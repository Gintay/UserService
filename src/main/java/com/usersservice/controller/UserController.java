package com.usersservice.controller;

import com.usersservice.dto.UserDto;
import com.usersservice.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    @GetMapping()
    public List<UserDto> getAllUsers(){
        return usersService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public UserDto getUser(@PathVariable("id") Integer id){
        return usersService.getUser(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto saveUser(@RequestBody UserDto userDto){
        return usersService.saveUser(userDto);
    }

}
