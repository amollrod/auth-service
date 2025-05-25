package com.tfg.auth.application.services;

import com.tfg.auth.application.dto.CreateUserRequest;
import com.tfg.auth.application.dto.UpdateUserRequest;
import com.tfg.auth.application.dto.UserResponse;
import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.services.UserDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDomainService domainService;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        domainService = mock(UserDomainService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(domainService, passwordEncoder);
    }

    @Test
    @DisplayName("Should create a user and encode password")
    void shouldCreateUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("new@user.com")
                .password("plain")
                .roles(Set.of("ADMIN"))
                .build();

        AppUser createdUser = AppUser.builder()
                .email("new@user.com")
                .password("hashed")
                .enabled(true)
                .roles(Set.of(Role.builder().name("ADMIN").build()))
                .build();

        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(domainService.createUser("new@user.com", "hashed", Set.of("ADMIN")))
                .thenReturn(createdUser);

        UserResponse response = userService.createUser(request);

        assertThat(response.getEmail()).isEqualTo("new@user.com");
    }

    @Test
    @DisplayName("Should update user if password is present")
    void shouldUpdateUserWithPassword() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .password("new-pwd")
                .enabled(true)
                .roles(Set.of("ADMIN"))
                .build();

        AppUser updated = AppUser.builder()
                .email("some@user.com")
                .password("new-pwd-enc")
                .enabled(true)
                .roles(Set.of(Role.builder().name("ADMIN").build()))
                .build();

        when(passwordEncoder.encode("new-pwd")).thenReturn("new-pwd-enc");
        when(domainService.updateUser("some@user.com", "new-pwd-enc", true, Set.of("ADMIN")))
                .thenReturn(updated);

        UserResponse response = userService.updateUser("some@user.com", request);

        assertThat(response.getEmail()).isEqualTo("some@user.com");
        assertThat(response.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Should update user with null password if blank")
    void shouldUpdateUserWithoutPassword() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .password("   ")
                .enabled(false)
                .roles(Set.of("USER"))
                .build();

        AppUser updated = AppUser.builder()
                .email("test@nochange.com")
                .password("unchanged")
                .enabled(false)
                .roles(Set.of(Role.builder().name("USER").build()))
                .build();

        when(domainService.updateUser("test@nochange.com", null, false, Set.of("USER")))
                .thenReturn(updated);

        UserResponse response = userService.updateUser("test@nochange.com", request);

        assertThat(response.getEmail()).isEqualTo("test@nochange.com");
        assertThat(response.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Should delete user by email")
    void shouldDeleteUser() {
        userService.deleteUser("bye@user.com");
        verify(domainService).deleteUser("bye@user.com");
    }

    @Test
    @DisplayName("Should fetch all users")
    void shouldGetAllUsers() {
        List<AppUser> users = List.of(
                AppUser.builder().email("a@x").password("p").build(),
                AppUser.builder().email("b@x").password("q").build()
        );

        when(domainService.getAllUsers()).thenReturn(users);

        List<UserResponse> result = userService.getAllUsers();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should get user by email")
    void shouldGetUserByEmail() {
        AppUser user = AppUser.builder().email("one@x").password("secret").build();
        when(domainService.getUser("one@x")).thenReturn(user);

        UserResponse result = userService.getUser("one@x");

        assertThat(result.getEmail()).isEqualTo("one@x");
    }
}
