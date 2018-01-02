package com.usersservice.dao;

import com.usersservice.domain.User;

import java.util.List;

public interface UserDAO {
    List<User> getAll();
    User get(Integer id);
    User save(User user);
}
