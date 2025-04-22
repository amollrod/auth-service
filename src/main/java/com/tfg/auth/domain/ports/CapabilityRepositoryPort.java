package com.tfg.auth.domain.ports;

import com.tfg.auth.domain.models.Capability;

import java.util.List;
import java.util.Optional;

public interface CapabilityRepositoryPort {
    Optional<Capability> findById(String id);
    List<Capability> findAll();
    Capability save(Capability capability);
    boolean existsById(String id);
}
