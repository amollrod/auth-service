package com.tfg.auth.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class AppUserTest {

    private Role adminRole;
    private Role userRole;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        adminRole = Role.builder()
                .name("ADMIN")
                .capabilities(Set.of(Capability.CREATE_USER, Capability.DELETE_USER))
                .build();

        userRole = Role.builder()
                .name("USER")
                .capabilities(Set.of(Capability.VIEW_USERS))
                .build();

        appUser = AppUser.builder()
                .email("test@example.com")
                .password("secret")
                .roles(Set.of(adminRole, userRole))
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Should return true if user has capability")
    void shouldHaveCapability() {
        assertThat(appUser.hasCapability(Capability.CREATE_USER)).isTrue();
        assertThat(appUser.hasCapability(Capability.VIEW_USERS)).isTrue();
    }

    @Test
    @DisplayName("Should return false if user does not have capability")
    void shouldNotHaveCapability() {
        assertThat(appUser.hasCapability(Capability.UPDATE_USER)).isFalse();
    }

    @Test
    @DisplayName("Should return true if user has role")
    void shouldHaveRole() {
        assertThat(appUser.hasRole("ADMIN")).isTrue();
        assertThat(appUser.hasRole("USER")).isTrue();
    }

    @Test
    @DisplayName("Should return false if user does not have role")
    void shouldNotHaveRole() {
        assertThat(appUser.hasRole("MODERATOR")).isFalse();
    }

    @Test
    @DisplayName("Should enable and disable user")
    void shouldEnableAndDisableUser() {
        appUser.disable();
        assertThat(appUser.isEnabled()).isFalse();

        appUser.enable();
        assertThat(appUser.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Should assign new roles")
    void shouldAssignRoles() {
        Role mod = Role.builder()
                .name("MODERATOR")
                .capabilities(Set.of(Capability.VIEW_USERS))
                .build();

        appUser.assignRoles(Set.of(mod));
        assertThat(appUser.getRoles()).containsExactly(mod);
    }

    @Test
    @DisplayName("Should clear roles if null is passed")
    void shouldClearRolesOnNull() {
        appUser.assignRoles(null);
        assertThat(appUser.getRoles()).isEmpty();
    }
}
