package com.tfg.auth.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.auth.application.dto.CreateRoleRequest;
import com.tfg.auth.application.dto.UpdateRoleRequest;
import com.tfg.auth.application.dto.RoleResponse;
import com.tfg.auth.application.services.RoleService;
import com.tfg.auth.domain.models.Capability;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
@Import(AuthorizationSecurityConfig.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @MockitoBean
    private MongoUserDetailsService mongoUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private RoleResponse roleResponse;

    @BeforeEach
    void setUp() {
        roleResponse = RoleResponse.builder()
                .name("ADMIN")
                .capabilities(Set.of(Capability.CREATE_USER, Capability.DELETE_USER))
                .build();
    }

    @Test
    @WithMockUser(authorities = "CREATE_ROLE")
    @DisplayName("Should create role")
    void shouldCreateRole() throws Exception {
        CreateRoleRequest request = CreateRoleRequest.builder()
                .name("ADMIN")
                .capabilities(Set.of(Capability.CREATE_USER, Capability.DELETE_USER))
                .build();

        when(roleService.createRole(any())).thenReturn(roleResponse);

        mockMvc.perform(post("/roles")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @WithMockUser(authorities = "UPDATE_ROLE")
    @DisplayName("Should update role")
    void shouldUpdateRole() throws Exception {
        UpdateRoleRequest request = UpdateRoleRequest.builder()
                .capabilities(Set.of(Capability.CREATE_USER))
                .build();

        when(roleService.updateRole(eq("ADMIN"), any())).thenReturn(roleResponse);

        mockMvc.perform(put("/roles/ADMIN")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @WithMockUser(authorities = "UPDATE_ROLE")
    @DisplayName("Should add capability")
    void shouldAddCapability() throws Exception {
        when(roleService.addCapability("ADMIN", Capability.CREATE_USER)).thenReturn(roleResponse);

        mockMvc.perform(patch("/roles/ADMIN/capabilities/add")
                        .with(csrf())
                        .param("capability", "CREATE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilities").isArray());
    }

    @Test
    @WithMockUser(authorities = "UPDATE_ROLE")
    @DisplayName("Should remove capability")
    void shouldRemoveCapability() throws Exception {
        when(roleService.removeCapability("ADMIN", Capability.DELETE_USER)).thenReturn(roleResponse);

        mockMvc.perform(patch("/roles/ADMIN/capabilities/remove")
                        .with(csrf())
                        .param("capability", "DELETE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capabilities").isArray());
    }

    @Test
    @WithMockUser(authorities = "UPDATE_ROLE")
    @DisplayName("Should clear capabilities")
    void shouldClearCapabilities() throws Exception {
        when(roleService.clearCapabilities("ADMIN")).thenReturn(roleResponse);

        mockMvc.perform(patch("/roles/ADMIN/capabilities/clear").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @WithMockUser(authorities = "VIEW_ROLES")
    @DisplayName("Should return role by name")
    void shouldReturnRoleByName() throws Exception {
        when(roleService.getRole("ADMIN")).thenReturn(roleResponse);

        mockMvc.perform(get("/roles/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @WithMockUser(authorities = "VIEW_ROLES")
    @DisplayName("Should return all roles")
    void shouldReturnAllRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(List.of(roleResponse));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ADMIN"));
    }

    @Test
    @WithMockUser(authorities = "DELETE_ROLE")
    @DisplayName("Should delete role")
    void shouldDeleteRole() throws Exception {
        mockMvc.perform(delete("/roles/ADMIN").with(csrf()))
                .andExpect(status().isNoContent());

        verify(roleService).deleteRole("ADMIN");
    }
}
