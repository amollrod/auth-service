package com.tfg.auth.domain.services;

import com.tfg.auth.domain.exceptions.RoleAlreadyExistsException;
import com.tfg.auth.domain.exceptions.RoleNotFoundException;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleDomainServiceTest {

    private RoleRepositoryPort roleRepo;
    private RoleDomainService service;

    @BeforeEach
    void setUp() {
        roleRepo = mock(RoleRepositoryPort.class);
        service = new RoleDomainService(roleRepo);
    }

    @Test
    @DisplayName("Should create new role")
    void shouldCreateRole() {
        String name = "MODERATOR";
        Set<Capability> capabilities = Set.of(Capability.VIEW_USERS);
        when(roleRepo.existsByName(name)).thenReturn(false);
        when(roleRepo.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        Role result = service.createRole(name, capabilities);

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getCapabilities()).containsExactlyInAnyOrderElementsOf(capabilities);
    }

    @Test
    @DisplayName("Should throw if role already exists")
    void shouldThrowIfRoleExists() {
        when(roleRepo.existsByName("ADMIN")).thenReturn(true);

        assertThatThrownBy(() -> service.createRole("ADMIN", Set.of()))
                .isInstanceOf(RoleAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Should update role capabilities")
    void shouldUpdateRole() {
        Role role = new Role("EDITOR", new HashSet<>(Set.of(Capability.VIEW_USERS)));
        when(roleRepo.findByName("EDITOR")).thenReturn(Optional.of(role));
        when(roleRepo.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        Set<Capability> newCaps = Set.of(Capability.UPDATE_USER);
        Role result = service.updateRole("EDITOR", newCaps);

        assertThat(result.getCapabilities()).containsOnly(Capability.UPDATE_USER);
    }

    @Test
    @DisplayName("Should add capability to role")
    void shouldAddCapabilityToRole() {
        Role role = new Role("USER", new HashSet<>());
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(role));
        when(roleRepo.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        Role result = service.addCapabilityToRole("USER", Capability.CREATE_USER);

        assertThat(result.getCapabilities()).contains(Capability.CREATE_USER);
    }

    @Test
    @DisplayName("Should remove capability from role")
    void shouldRemoveCapabilityFromRole() {
        Role role = new Role("MOD", new HashSet<>(Set.of(Capability.VIEW_USERS)));
        when(roleRepo.findByName("MOD")).thenReturn(Optional.of(role));
        when(roleRepo.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        Role result = service.removeCapabilityFromRole("MOD", Capability.VIEW_USERS);

        assertThat(result.getCapabilities()).doesNotContain(Capability.VIEW_USERS);
    }

    @Test
    @DisplayName("Should clear capabilities from role")
    void shouldClearCapabilities() {
        Role role = new Role("ANY", new HashSet<>(Set.of(Capability.VIEW_USERS, Capability.CREATE_USER)));
        when(roleRepo.findByName("ANY")).thenReturn(Optional.of(role));
        when(roleRepo.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        Role result = service.clearCapabilities("ANY");

        assertThat(result.getCapabilities()).isEmpty();
    }

    @Test
    @DisplayName("Should get all roles")
    void shouldGetAllRoles() {
        List<Role> roles = List.of(new Role("A", Set.of()));
        when(roleRepo.findAll()).thenReturn(roles);

        assertThat(service.getAllRoles()).containsExactlyElementsOf(roles);
    }

    @Test
    @DisplayName("Should get role by name")
    void shouldGetRoleByName() {
        Role role = new Role("ADMIN", Set.of());
        when(roleRepo.findByName("ADMIN")).thenReturn(Optional.of(role));

        Role result = service.getRole("ADMIN");

        assertThat(result).isEqualTo(role);
    }

    @Test
    @DisplayName("Should delete role by name")
    void shouldDeleteRole() {
        when(roleRepo.existsByName("ADMIN")).thenReturn(true);

        service.deleteRole("ADMIN");

        verify(roleRepo).deleteByName("ADMIN");
    }

    @Test
    @DisplayName("Should throw when deleting non-existent role")
    void shouldFailToDeleteRole() {
        when(roleRepo.existsByName("NONE")).thenReturn(false);

        assertThatThrownBy(() -> service.deleteRole("NONE"))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw when role not found")
    void shouldFailToFindRole() {
        when(roleRepo.findByName("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRole("ghost"))
                .isInstanceOf(RoleNotFoundException.class);
    }
}
