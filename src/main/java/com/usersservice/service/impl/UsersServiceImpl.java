package com.usersservice.service.impl;

import com.usersservice.dao.UserDAO;
import com.usersservice.domain.User;
import com.usersservice.dto.UserDto;
import com.usersservice.service.UsersService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    private UserDAO userDAO;

    public UsersServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserDto> getAllUsers(){
        List<User> users = userDAO.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(x -> userDtos.add(UserDto.toDto(x)));
        return userDtos;
    }

    public UserDto getUser(Integer id){
        User user = userDAO.get(id);
        return UserDto.toDto(user);
    }

    public UserDto saveUser(UserDto userDto){
        User user = UserDto.fromDto(userDto);
        User resultUser = userDAO.save(user);
        return UserDto.toDto(resultUser);
    }
}
