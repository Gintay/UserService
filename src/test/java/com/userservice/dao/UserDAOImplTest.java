package com.userservice.dao;

import com.datastax.driver.core.*;
import com.usersservice.dao.impl.UserDAOImpl;
import com.usersservice.domain.User;
import com.usersservice.util.UserServiceConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDAOImplTest {

    @InjectMocks
    private UserDAOImpl userDAO;

    @Mock
    private Session session;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private BoundStatement boundGetUser;

    @Mock
    private ResultSet resultSet;

    @Mock
    private Row row;

    private int expectedId;
    private String expectedUsername;
    private String expectedPassword;
    private String expectedEmail;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        expectedId = 1;
        expectedUsername = "user";
        expectedPassword = "password";
        expectedEmail = "email@mail.com";

        when(session.prepare(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.bind()).thenReturn(boundGetUser);
        when(session.execute(any(BoundStatement.class))).thenReturn(resultSet);
        when(resultSet.one()).thenReturn(row);

        when(row.getInt(UserServiceConstants.COLUMN_ID)).thenReturn(expectedId);
        when(row.getString(UserServiceConstants.COLUMN_USERNAME)).thenReturn(expectedUsername);
        when(row.getString(UserServiceConstants.COLUMN_PASSWORD)).thenReturn(expectedPassword);
        when(row.getString(UserServiceConstants.COLUMN_EMAIL)).thenReturn(expectedEmail);
    }

    @Test
    public void testGetAll_ShouldReturnUsersList(){
        List<Row> rows = new ArrayList<>();
        rows.add(row);

        when(resultSet.all()).thenReturn(rows);

        List<User> users = userDAO.getAll();

        assertEquals(rows.size(), users.size());
        assertEquals(expectedId, users.get(0).getId().intValue());
        assertEquals(expectedUsername, users.get(0).getUsername());
        assertEquals(expectedPassword, users.get(0).getPassword());
        assertEquals(expectedEmail, users.get(0).getEmail());
    }

    @Test
    public void testGet_ShouldReturnUser(){
        User user = userDAO.get(1);

        assertEquals(expectedId, user.getId().intValue());
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword,user.getPassword());
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    public void testSave_ShouldReturnNull(){
        when(resultSet.wasApplied()).thenReturn(false);

        User user = mock(User.class);

        User resultUser = userDAO.save(user);

        assertNull(resultUser);
    }

    @Test
    public void testSave_ShouldReturnUser(){
        when(resultSet.wasApplied()).thenReturn(true);

        User user = mock(User.class);

        User resultUser = userDAO.save(user);

        assertEquals(expectedId, resultUser.getId().intValue());
        assertEquals(expectedUsername, resultUser.getUsername());
        assertEquals(expectedPassword,resultUser.getPassword());
        assertEquals(expectedEmail, resultUser.getEmail());
    }
}
