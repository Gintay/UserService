package com.usersservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;

@SpringBootApplication
public class Launcher {
    public static void main(String[] args) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", 8182);
        SpringApplicationBuilder application = new SpringApplicationBuilder().sources(Launcher.class).properties(props);
        application.run(args);
    }
}