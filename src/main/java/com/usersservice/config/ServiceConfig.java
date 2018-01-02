package com.usersservice.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.usersservice.dao.UserDAO;
import com.usersservice.dao.impl.UserDAOImpl;
import com.usersservice.service.UsersService;
import com.usersservice.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Value("${cassandra.url}")
    private String cassandraUrl; // = "127.0.0.1";
    @Value("${cassandra.port}")
    private Integer cassandraPort; // = 9042;
    @Value("${cassandra.keyspace}")
    private String cassandraKeyspace; // = education;

    @Bean
    public Cluster cassandraCluster(){
        return Cluster.builder().addContactPoint(cassandraUrl).withPort(cassandraPort).build();
    }

    @Bean
    public Session cassandraSession(@Autowired Cluster cluster){
        return cluster.connect(cassandraKeyspace);
    }

    @Bean
    public UserDAO userDAO(@Autowired Session session){
        return new UserDAOImpl(session);
    }

//    @Bean
//    public UsersService usersService(@Autowired UserDAO userDAO){
//        return new UsersServiceImpl(userDAO);
//    }
}
