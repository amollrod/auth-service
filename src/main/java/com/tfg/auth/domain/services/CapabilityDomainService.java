package com.tfg.auth.domain.services;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.ports.CapabilityRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CapabilityDomainService {

    private final CapabilityRepositoryPort capabilityRepository;

    public List<Capability> getAllCapabilities() {
        return capabilityRepository.findAll();
    }
}
