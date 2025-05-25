package com.tfg.auth.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.auth.application.dto.CapabilityResponse;
import com.tfg.auth.application.services.CapabilityService;
import com.tfg.auth.infrastructure.adapters.security.MongoUserDetailsService;
import com.tfg.auth.infrastructure.config.AuthorizationSecurityConfig;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CapabilityController.class)
@Import(AuthorizationSecurityConfig.class)
class CapabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CapabilityService capabilityService;

    @MockitoBean
    private MongoUserDetailsService mongoUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "VIEW_CAPABILITIES")
    @DisplayName("Should return all capabilities")
    void shouldReturnAllCapabilities() throws Exception {
        List<CapabilityResponse> responses = List.of(
                CapabilityResponse.builder().name("CREATE_USER").description("Allows creation of users").build(),
                CapabilityResponse.builder().name("VIEW_USERS").description("Allows viewing users").build()
        );

        when(capabilityService.getAllCapabilities()).thenReturn(responses);

        mockMvc.perform(get("/capabilities").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("CREATE_USER"));
    }
}
