package com.tfg.auth.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .name("USER")
                .capabilities(EnumSet.of(Capability.VIEW_USERS))
                .build();
    }

    @Test
    @DisplayName("Should return true if role has capability")
    void shouldHaveCapability() {
        assertThat(role.hasCapability(Capability.VIEW_USERS)).isTrue();
    }

    @Test
    @DisplayName("Should return false if role does not have capability")
    void shouldNotHaveCapability() {
        assertThat(role.hasCapability(Capability.DELETE_USER)).isFalse();
    }

    @Test
    @DisplayName("Should add capability")
    void shouldAddCapability() {
        role.addCapability(Capability.CREATE_USER);
        assertThat(role.getCapabilities()).contains(Capability.CREATE_USER);
    }

    @Test
    @DisplayName("Should remove capability")
    void shouldRemoveCapability() {
        role.removeCapability(Capability.VIEW_USERS);
        assertThat(role.getCapabilities()).doesNotContain(Capability.VIEW_USERS);
    }

    @Test
    @DisplayName("Should clear capabilities")
    void shouldClearCapabilities() {
        role.clearCapabilities();
        assertThat(role.getCapabilities()).isEmpty();
    }

    @Test
    @DisplayName("Should replace capabilities")
    void shouldSetCapabilities() {
        Set<Capability> newSet = Set.of(Capability.UPDATE_USER, Capability.FIND_PACKAGE);
        role.setCapabilities(newSet);
        assertThat(role.getCapabilities()).containsExactlyInAnyOrderElementsOf(newSet);
    }

    @Test
    @DisplayName("Should clear if new set is empty")
    void shouldClearIfNewSetIsEmpty() {
        role.setCapabilities(Set.of());
        assertThat(role.getCapabilities()).isEmpty();
    }
}
