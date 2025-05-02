package com.tfg.auth.domain.services;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.ports.CapabilityRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CapabilityDomainService {

    private final CapabilityRepositoryPort capabilityRepository;

    public List<Capability> getAllCapabilities() {
        return capabilityRepository.findAll();
    }
}
