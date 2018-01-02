package com.usersservice.service;

import com.usersservice.dto.UserDto;

import java.util.List;

public interface UsersService {
    List<UserDto> getAllUsers();
    UserDto getUser(Integer id);
    UserDto saveUser(UserDto userDto);
}
