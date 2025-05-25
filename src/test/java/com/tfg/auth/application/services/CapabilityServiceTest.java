package com.tfg.auth.application.services;

import com.tfg.auth.application.dto.CapabilityResponse;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.services.CapabilityDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CapabilityServiceTest {

    private CapabilityDomainService domainService;
    private CapabilityService capabilityService;

    @BeforeEach
    void setUp() {
        domainService = mock(CapabilityDomainService.class);
        capabilityService = new CapabilityService(domainService);
    }

    @Test
    @DisplayName("Should return all capabilities")
    void shouldReturnAllCapabilities() {
        List<Capability> capabilities = List.of(
                Capability.VIEW_USERS,
                Capability.CREATE_USER,
                Capability.UPDATE_USER
        );

        when(domainService.getAllCapabilities()).thenReturn(capabilities);

        List<CapabilityResponse> responses = capabilityService.getAllCapabilities();

        assertThat(responses).hasSize(3);
        assertThat(responses).extracting(CapabilityResponse::getName)
                .containsExactlyInAnyOrder("VIEW_USERS", "CREATE_USER", "UPDATE_USER");
    }
}
