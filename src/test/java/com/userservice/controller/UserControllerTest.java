package com.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usersservice.controller.UserController;
import com.usersservice.dto.UserDto;
import com.usersservice.service.impl.UsersServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UsersServiceImpl usersService;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private UserDto userDto;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mapper = new ObjectMapper();

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("username1");
        userDto.setPassword("password");
        userDto.setEmail("email1@mail.com");
    }

    @Test
    public void testGetAllUsers_ShouldReturnUsersDtoList() throws Exception {
        UserDto secondUserDto = new UserDto();
        secondUserDto.setId(2);
        secondUserDto.setUsername("username2");
        secondUserDto.setPassword("password");
        secondUserDto.setEmail("email2@mail.com");

        final int FIRST_ELEMENT = 0;
        final int SECOND_ELEMENT = 1;

        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        users.add(secondUserDto);

        when(usersService.getAllUsers()).thenReturn(users);

        MvcResult mvcResult = mockMvc.perform(
                get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();

        List<UserDto> userDtoList = Arrays.asList(mapper.readValue(responseString, UserDto[].class));

        assertEquals(users.size(), userDtoList.size());
        assertEquals(users.get(FIRST_ELEMENT).getId(), userDtoList.get(FIRST_ELEMENT).getId());
        assertEquals(users.get(FIRST_ELEMENT).getUsername(), userDtoList.get(FIRST_ELEMENT).getUsername());
        assertEquals(users.get(FIRST_ELEMENT).getPassword(), userDtoList.get(FIRST_ELEMENT).getPassword());
        assertEquals(users.get(FIRST_ELEMENT).getEmail(), userDtoList.get(FIRST_ELEMENT).getEmail());

        assertEquals(users.get(SECOND_ELEMENT).getId(), userDtoList.get(SECOND_ELEMENT).getId());
        assertEquals(users.get(SECOND_ELEMENT).getUsername(), userDtoList.get(SECOND_ELEMENT).getUsername());
        assertEquals(users.get(SECOND_ELEMENT).getPassword(), userDtoList.get(SECOND_ELEMENT).getPassword());
        assertEquals(users.get(SECOND_ELEMENT).getEmail(), userDtoList.get(SECOND_ELEMENT).getEmail());
    }

    @Test
    public void testGetUser_ShouldReturnCorrectUserDto() throws Exception {
        when(usersService.getUser(any(Integer.class))).thenReturn(userDto);

        MvcResult mvcResult = mockMvc.perform(
                get("/users/1"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        UserDto resultUserDto = mapper.readValue(responseString, UserDto.class);

        assertEquals(userDto.getId(), resultUserDto.getId());
        assertEquals(userDto.getUsername(), resultUserDto.getUsername());
        assertEquals(userDto.getPassword(), resultUserDto.getPassword());
        assertEquals(userDto.getEmail(), resultUserDto.getEmail());
    }

    @Test
    public void testSaveUser_ShouldReturnSavedUserDto() throws Exception {
        UserDto parameterUserDto = new UserDto();
        String jsonUserDto = mapper.writeValueAsString(parameterUserDto);

        when(usersService.saveUser(any(UserDto.class))).thenReturn(userDto);
        MvcResult mvcResult = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserDto))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        UserDto resultUserDto = mapper.readValue(responseString, UserDto.class);

        assertEquals(userDto.getId(), resultUserDto.getId());
        assertEquals(userDto.getUsername(), resultUserDto.getUsername());
        assertEquals(userDto.getPassword(), resultUserDto.getPassword());
        assertEquals(userDto.getEmail(), resultUserDto.getEmail());
    }
}
