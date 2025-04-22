package com.tfg.auth.infrastructure.adapters.persistence.adapter;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.ports.CapabilityRepositoryPort;
import com.tfg.auth.infrastructure.adapters.persistence.mapper.CapabilityMapper;
import com.tfg.auth.infrastructure.adapters.persistence.repository.CapabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CapabilityRepositoryAdapter implements CapabilityRepositoryPort {

    private final CapabilityRepository capabilityRepository;

    @Override
    public Optional<Capability> findById(String id) {
        return capabilityRepository.findById(id).map(CapabilityMapper::toDomain);
    }

    @Override
    public List<Capability> findAll() {
        return capabilityRepository.findAll()
                .stream()
                .map(CapabilityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Capability save(Capability capability) {
        return CapabilityMapper.toDomain(
                capabilityRepository.save(CapabilityMapper.toDocument(capability))
        );
    }

    @Override
    public boolean existsById(String id) {
        return capabilityRepository.existsById(id);
    }
}
