package com.tfg.auth.domain.services;

import com.tfg.auth.domain.exceptions.RoleNotFoundException;
import com.tfg.auth.domain.exceptions.UserAlreadyExistsException;
import com.tfg.auth.domain.exceptions.UserNotFoundException;
import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import com.tfg.auth.domain.ports.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDomainServiceTest {

    private UserRepositoryPort userRepo;
    private RoleRepositoryPort roleRepo;
    private UserDomainService service;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepositoryPort.class);
        roleRepo = mock(RoleRepositoryPort.class);
        service = new UserDomainService(userRepo, roleRepo);
    }

    @Test
    @DisplayName("Should create new user with non-encoded password and roles")
    void shouldCreateUser() {
        String email = "user@example.com";
        String password = "non-encoded";
        Role role = new Role("USER", Set.of(Capability.VIEW_USERS));
        when(userRepo.existsByEmail(email)).thenReturn(false);
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppUser result = service.createUser(email, password, Set.of("USER"));

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);
        assertThat(result.getRoles()).contains(role);
        assertThat(result.getRoles().stream().map(Role::getCapabilities)
                .flatMap(Collection::stream).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(Capability.VIEW_USERS);
        assertThat(result.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Should throw if user already exists")
    void shouldThrowIfUserExists() {
        when(userRepo.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.createUser("existing@example.com", "pwd", Set.of()))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Should update user info")
    void shouldUpdateUser() {
        String email = "user@example.com";
        AppUser user = AppUser.builder().email(email).password("old").enabled(false).roles(Set.of()).build();
        Role newRole = new Role("ADMIN", Set.of());

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(roleRepo.findByName("ADMIN")).thenReturn(Optional.of(newRole));
        when(userRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppUser updated = service.updateUser(email, "new-pwd", true, Set.of("ADMIN"));

        assertThat(updated.getPassword()).isEqualTo("new-pwd");
        assertThat(updated.getRoles()).contains(newRole);
        assertThat(updated.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Should throw if user to update does not exist")
    void shouldFailToUpdateNonexistentUser() {
        when(userRepo.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateUser("ghost@example.com", "x", true, Set.of()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should delete existing user")
    void shouldDeleteUser() {
        when(userRepo.existsByEmail("delete@example.com")).thenReturn(true);

        service.deleteUser("delete@example.com");

        verify(userRepo).deleteByEmail("delete@example.com");
    }

    @Test
    @DisplayName("Should throw when deleting non-existent user")
    void shouldThrowOnDeleteNonexistentUser() {
        when(userRepo.existsByEmail("ghost@example.com")).thenReturn(false);

        assertThatThrownBy(() -> service.deleteUser("ghost@example.com"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should fetch all users")
    void shouldReturnAllUsers() {
        List<AppUser> users = List.of(AppUser.builder().email("a@x").password("p").build());
        when(userRepo.findAll()).thenReturn(users);

        assertThat(service.getAllUsers()).hasSize(1);
    }

    @Test
    @DisplayName("Should get user by email")
    void shouldReturnUserByEmail() {
        AppUser user = AppUser.builder().email("x@y").password("p").build();
        when(userRepo.findByEmail("x@y")).thenReturn(Optional.of(user));

        AppUser result = service.getUser("x@y");

        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("Should fail when user email not found")
    void shouldFailToFindUserByEmail() {
        when(userRepo.findByEmail("ghost@x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUser("ghost@x"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw RoleNotFoundException if any role is not found")
    void shouldThrowWhenRoleNotFound() {
        when(userRepo.existsByEmail("x")).thenReturn(false);
        when(roleRepo.findByName("BAD")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createUser("x", "p", Set.of("BAD")))
                .isInstanceOf(RoleNotFoundException.class);
    }
}
