package com.consulteer.shoppingstore.controllers;

import com.consulteer.shoppingstore.dtos.CreateUserDto;
import com.consulteer.shoppingstore.dtos.UpdateUserDto;
import com.consulteer.shoppingstore.exceptions.ResourceNotFoundException;
import com.consulteer.shoppingstore.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @InjectMocks
    private UserController underTest;
    @Mock
    private UserService userService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        //given - when - then
        mockMvc.perform(
                        get("/api/v1/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserById() throws Exception {
        //given - when - then
        mockMvc.perform(
                        get("/api/v1/users/{user-id}", anyLong()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetUserById() throws Exception {
        //given
        when(userService.getUserById(anyLong())).thenThrow(ResourceNotFoundException.class);

        //when - then
        mockMvc.perform(
                        get("/api/v1/users/{user-id}", anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUser() throws Exception {
        //given
        var createUserDto = new CreateUserDto("marko123",
                "12345",
                "Marko",
                "Markovic",
                "marko@gmail.com",
                "ROLE_DUMMY");

        var content = objectMapper.writeValueAsString(createUserDto);

        //when = then
        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCreateUser() throws Exception {
        //given
        var createUserDto = new CreateUserDto("marko123",
                "12345",
                "Marko",
                "Markovic",
                "marko@gmail.com",
                "ROLE_DUMMY");

        var content = objectMapper.writeValueAsString(createUserDto);

        when(userService.createUser(any())).thenThrow(ResourceNotFoundException.class);

        //when - then
        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        //given
        var updateUserDto = new UpdateUserDto("markomarkovic123",
                "password",
                "markomarkovic@gmail.com");

        var content = objectMapper.writeValueAsString(updateUserDto);

        //when - then
        mockMvc.perform(
                        put("/api/v1/users/{user-id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateUser() throws Exception {
        //given
        var updateUserDto = new UpdateUserDto("markomarkovic123",
                "password",
                "markomarkovic@gmail.com");

        var content = objectMapper.writeValueAsString(updateUserDto);

        when(userService.updateUserInfo(any(), anyLong())).thenThrow(ResourceNotFoundException.class);

        //when - then
        mockMvc.perform(
                        put("/api/v1/users/{user-id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        //given - when - then
        mockMvc.perform(
                        delete("/api/v1/users/{user-id}", anyLong()))
                .andExpect(status().isOk());
    }
}