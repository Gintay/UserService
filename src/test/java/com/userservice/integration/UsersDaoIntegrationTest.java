package com.userservice.integration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.usersservice.dao.UserDAO;
import com.usersservice.dao.impl.UserDAOImpl;
import com.usersservice.domain.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class UsersDaoIntegrationTest {
    private static final String CASSANDRA_URL = "127.0.0.1";
    private static final Integer CASSANDRA_PORT = 9042;
    private static final String KEYSPACE_NAME = "education_test";
    private static final String TABLE_NAME = "users";

    private static UserDAO userDAO;
    private static Session cassandraSession;
    private static Mapper<User> userMapper;
    private static List<User> users;

    @BeforeClass
    public static void init(){
        Cluster cluster = Cluster.builder().addContactPoint(CASSANDRA_URL)
                .withPort(CASSANDRA_PORT).build();
        cassandraSession = cluster.connect();
        userDAO = new UserDAOImpl(cassandraSession);

        String queryCreateKeyspace = String.format("CREATE KEYSPACE IF NOT EXISTS %s WITH REPLICATION "
                + "= {'class':'SimpleStrategy', 'replication_factor': 1};", KEYSPACE_NAME);
        cassandraSession.execute(queryCreateKeyspace);
        cassandraSession.execute("USE " + KEYSPACE_NAME);

        String queryCreateTable = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id int PRIMARY KEY,username text,password text,email text);", TABLE_NAME);
        cassandraSession.execute(queryCreateTable);

        User testUser1 = new User();
        testUser1.setId(1);
        testUser1.setUsername("Igor1");
        testUser1.setPassword("password1");
        testUser1.setEmail("email@mail.com");

        User testUser2 = new User();
        testUser2.setId(2);
        testUser2.setUsername("Igor2");
        testUser2.setPassword("password2");
        testUser2.setEmail("email2@mail.com");

        User testUser3 = new User();
        testUser3.setId(3);
        testUser3.setUsername("Igor3");
        testUser3.setPassword("password3");
        testUser3.setEmail("emai3@mail.com");

        users = new ArrayList<>();
        users.add(testUser1);
        users.add(testUser2);
        users.add(testUser3);

        MappingManager mappingManager = new MappingManager(cassandraSession);
        userMapper = mappingManager.mapper(User.class);

        users.forEach(user -> userMapper.save(user));
    }

    @AfterClass
    public static void destroy() {
        String queryDropTestTable = String.format("DROP TABLE %s.%s", KEYSPACE_NAME, TABLE_NAME);
        String queryDropTestKeyspace = String.format("DROP KEYSPACE %s", KEYSPACE_NAME);
        cassandraSession.execute(queryDropTestTable);
        cassandraSession.execute(queryDropTestKeyspace);
        cassandraSession.close();
    }

    @Test
    public void testGetUser_ShouldReturnCorrectUser() throws Exception {
        final int firstUser = 1;
        User actualUser = userDAO.get(users.get(firstUser).getId());

        Assert.assertEquals(users.get(firstUser).getId(), actualUser.getId());
        Assert.assertEquals(users.get(firstUser).getUsername(), actualUser.getUsername());
        Assert.assertEquals(users.get(firstUser).getPassword(), actualUser.getPassword());
        Assert.assertEquals(users.get(firstUser).getEmail(), actualUser.getEmail());
    }

    @Test
    public void testGetUser_ShouldReturnAllUsers() throws Exception {
        List<User> actualUsers = userDAO.getAll();

        Assert.assertEquals(users.size(), actualUsers.size());
        for (int i = 0; i < users.size(); i++){
            User expectedUser = users.get(i);
            User actualUser = actualUsers.get(i);

            Assert.assertEquals(expectedUser.getId(), actualUser.getId());
            Assert.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
            Assert.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
            Assert.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        }
    }

    @Test
    public void testGetUser_ShouldReturnCorrectUserAfterSave() throws Exception {
        User savedUser = new User();
        savedUser.setId(4);
        savedUser.setUsername("Igor4");
        savedUser.setPassword("password");
        savedUser.setEmail("emai4@mail.com");

        User actualUser = userDAO.save(savedUser);

        // Have to delete saved user, because testGetUser_ShouldReturnAllUsers can fail
        userMapper.delete(savedUser);

        Assert.assertEquals(savedUser.getId(), actualUser.getId());
        Assert.assertEquals(savedUser.getUsername(), actualUser.getUsername());
        Assert.assertEquals(savedUser.getPassword(), actualUser.getPassword());
        Assert.assertEquals(savedUser.getEmail(), actualUser.getEmail());
    }
}
