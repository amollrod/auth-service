package com.tfg.auth.application.services;

import com.tfg.auth.application.dto.CreateRoleRequest;
import com.tfg.auth.application.dto.UpdateRoleRequest;
import com.tfg.auth.application.dto.RoleResponse;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.services.RoleDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    private RoleDomainService domainService;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        domainService = mock(RoleDomainService.class);
        roleService = new RoleService(domainService);
    }

    @Test
    @DisplayName("Should create a role")
    void shouldCreateRole() {
        CreateRoleRequest request = CreateRoleRequest.builder()
                .name("MODERATOR")
                .capabilities(Set.of(Capability.VIEW_USERS))
                .build();

        Role role = Role.builder()
                .name("MODERATOR")
                .capabilities(Set.of(Capability.VIEW_USERS))
                .build();

        when(domainService.createRole("MODERATOR", Set.of(Capability.VIEW_USERS))).thenReturn(role);

        RoleResponse response = roleService.createRole(request);

        assertThat(response.getName()).isEqualTo("MODERATOR");
        assertThat(response.getCapabilities()).contains(Capability.VIEW_USERS);
    }

    @Test
    @DisplayName("Should update role capabilities")
    void shouldUpdateRole() {
        UpdateRoleRequest request = UpdateRoleRequest.builder()
                .capabilities(Set.of(Capability.CREATE_USER))
                .build();

        Role role = Role.builder()
                .name("USER")
                .capabilities(Set.of(Capability.CREATE_USER))
                .build();

        when(domainService.updateRole("USER", Set.of(Capability.CREATE_USER))).thenReturn(role);

        RoleResponse response = roleService.updateRole("USER", request);

        assertThat(response.getCapabilities()).containsExactly(Capability.CREATE_USER);
    }

    @Test
    @DisplayName("Should add capability to role")
    void shouldAddCapability() {
        Role role = Role.builder()
                .name("ADMIN")
                .capabilities(Set.of(Capability.VIEW_USERS, Capability.CREATE_USER))
                .build();

        when(domainService.addCapabilityToRole("ADMIN", Capability.CREATE_USER)).thenReturn(role);

        RoleResponse response = roleService.addCapability("ADMIN", Capability.CREATE_USER);

        assertThat(response.getCapabilities()).contains(Capability.CREATE_USER);
    }

    @Test
    @DisplayName("Should remove capability from role")
    void shouldRemoveCapability() {
        Role role = Role.builder()
                .name("EDITOR")
                .capabilities(Set.of())
                .build();

        when(domainService.removeCapabilityFromRole("EDITOR", Capability.VIEW_USERS)).thenReturn(role);

        RoleResponse response = roleService.removeCapability("EDITOR", Capability.VIEW_USERS);

        assertThat(response.getCapabilities()).doesNotContain(Capability.VIEW_USERS);
    }

    @Test
    @DisplayName("Should clear all capabilities")
    void shouldClearCapabilities() {
        Role role = Role.builder()
                .name("TEMP")
                .capabilities(Set.of())
                .build();

        when(domainService.clearCapabilities("TEMP")).thenReturn(role);

        RoleResponse response = roleService.clearCapabilities("TEMP");

        assertThat(response.getCapabilities()).isEmpty();
    }

    @Test
    @DisplayName("Should return role by name")
    void shouldGetRoleByName() {
        Role role = Role.builder()
                .name("SUPPORT")
                .capabilities(Set.of(Capability.SEARCH_PACKAGES))
                .build();

        when(domainService.getRole("SUPPORT")).thenReturn(role);

        RoleResponse response = roleService.getRole("SUPPORT");

        assertThat(response.getName()).isEqualTo("SUPPORT");
    }

    @Test
    @DisplayName("Should return all roles")
    void shouldReturnAllRoles() {
        List<Role> roles = List.of(
                Role.builder().name("ADMIN").capabilities(Set.of()).build(),
                Role.builder().name("USER").capabilities(Set.of()).build()
        );

        when(domainService.getAllRoles()).thenReturn(roles);

        List<RoleResponse> responses = roleService.getAllRoles();

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(RoleResponse::getName).containsExactlyInAnyOrder("ADMIN", "USER");
    }

    @Test
    @DisplayName("Should delete role")
    void shouldDeleteRole() {
        roleService.deleteRole("MODERATOR");

        verify(domainService).deleteRole("MODERATOR");
    }
}
