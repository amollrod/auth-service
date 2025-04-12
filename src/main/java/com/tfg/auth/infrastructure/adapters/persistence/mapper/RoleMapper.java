package com.tfg.auth.infrastructure.adapters.persistence.mapper;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.infrastructure.adapters.persistence.document.RoleDocument;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {

    public static Role toDomain(RoleDocument document) {
        Set<Capability> capabilities = document.getCapabilities().stream()
                .map(Capability::valueOf)
                .collect(Collectors.toSet());

        return Role.builder()
                .name(document.getName())
                .capabilities(capabilities)
                .build();
    }

    public static RoleDocument toDocument(Role role) {
        Set<String> capabilityNames = role.getCapabilities().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return RoleDocument.builder()
                .name(role.getName())
                .capabilities(capabilityNames)
                .build();
    }
}
