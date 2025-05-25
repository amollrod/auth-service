package com.tfg.auth.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.auth.application.dto.CreateUserRequest;
import com.tfg.auth.application.dto.UpdateUserRequest;
import com.tfg.auth.application.dto.UserResponse;
import com.tfg.auth.application.services.UserService;
import com.tfg.auth.infrastructure.adapters.security.MongoUserDetailsService;
import com.tfg.auth.infrastructure.config.AuthorizationSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(AuthorizationSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MongoUserDetailsService mongoUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userResponse = UserResponse.builder()
                .email("user@example.com")
                .enabled(true)
                .roles(Set.of("ADMIN"))
                .build();
    }

    @Test
    @WithMockUser(authorities = "CREATE_USER")
    @DisplayName("Should create user")
    void shouldCreateUser() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("user@example.com")
                .password("secure")
                .roles(Set.of("ADMIN"))
                .build();

        when(userService.createUser(any())).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @WithMockUser(authorities = "VIEW_USERS")
    @DisplayName("Should return all users")
    void shouldGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userResponse));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"));
    }

    @Test
    @WithMockUser(authorities = "VIEW_USERS")
    @DisplayName("Should return user by email")
    void shouldGetUserByEmail() throws Exception {
        when(userService.getUser("user@example.com")).thenReturn(userResponse);

        mockMvc.perform(get("/users/user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @WithMockUser(authorities = "UPDATE_USER")
    @DisplayName("Should update user")
    void shouldUpdateUser() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .password("newpass")
                .enabled(true)
                .roles(Set.of("USER"))
                .build();

        when(userService.updateUser(eq("user@example.com"), any())).thenReturn(userResponse);

        mockMvc.perform(put("/users/user@example.com")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @WithMockUser(authorities = "DELETE_USER")
    @DisplayName("Should delete user")
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/user@example.com").with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser("user@example.com");
    }
}
