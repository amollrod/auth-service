package com.tfg.auth.domain.services;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.ports.CapabilityRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CapabilityDomainServiceTest {

    private CapabilityRepositoryPort repository;
    private CapabilityDomainService service;

    @BeforeEach
    void setUp() {
        repository = mock(CapabilityRepositoryPort.class);
        service = new CapabilityDomainService(repository);
    }

    @Test
    @DisplayName("Should return all capabilities from repository")
    void shouldReturnAllCapabilities() {
        List<Capability> expected = List.of(Capability.VIEW_USERS, Capability.CREATE_USER);
        when(repository.findAll()).thenReturn(expected);

        List<Capability> result = service.getAllCapabilities();

        assertThat(result).containsExactlyElementsOf(expected);
        verify(repository).findAll();
    }
}
