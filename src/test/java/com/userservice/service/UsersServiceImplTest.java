package com.userservice.service;

import com.usersservice.dao.impl.UserDAOImpl;
import com.usersservice.domain.User;
import com.usersservice.dto.UserDto;
import com.usersservice.service.impl.UsersServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersServiceImplTest {

    @InjectMocks
    private UsersServiceImpl usersService;

    @Mock
    private UserDAOImpl userDAO;

    private User user;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1);
        user.setUsername("username1");
        user.setPassword("password");
        user.setEmail("email1@mail.com");
    }

    @Test
    public void testGetAllUsers(){
        final int FIRST_ELEMENT = 0;

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userDAO.getAll()).thenReturn(users);

        List<UserDto> resultUsersDto = usersService.getAllUsers();

        assertEquals(users.size(), resultUsersDto.size());
        assertEquals(users.get(FIRST_ELEMENT).getId(), resultUsersDto.get(FIRST_ELEMENT).getId());
        assertEquals(users.get(FIRST_ELEMENT).getUsername(), resultUsersDto.get(FIRST_ELEMENT).getUsername());
        assertEquals(users.get(FIRST_ELEMENT).getPassword(), resultUsersDto.get(FIRST_ELEMENT).getPassword());
        assertEquals(users.get(FIRST_ELEMENT).getEmail(), resultUsersDto.get(FIRST_ELEMENT).getEmail());
    }

    @Test
    public void testGetUser(){
        when(userDAO.get(any(Integer.class))).thenReturn(user);

        UserDto resultUserDto = usersService.getUser(1);

        assertEquals(user.getId(), resultUserDto.getId());
        assertEquals(user.getUsername(), resultUserDto.getUsername());
        assertEquals(user.getPassword(), resultUserDto.getPassword());
        assertEquals(user.getEmail(), resultUserDto.getEmail());
    }

    @Test
    public void testSaveUser(){
        UserDto parameterUser = mock(UserDto.class);

        when(userDAO.save(any(User.class))).thenReturn(user);

        UserDto resultUserDto = usersService.saveUser(parameterUser);

        assertEquals(user.getId(), resultUserDto.getId());
        assertEquals(user.getUsername(), resultUserDto.getUsername());
        assertEquals(user.getPassword(), resultUserDto.getPassword());
        assertEquals(user.getEmail(), resultUserDto.getEmail());
    }

}
