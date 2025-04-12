package com.tfg.auth.infrastructure.adapters.persistence.mapper;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.infrastructure.adapters.persistence.document.CapabilityDocument;

public class CapabilityMapper {

    public static Capability toDomain(CapabilityDocument document) {
        return Capability.valueOf(document.getId());
    }

    public static CapabilityDocument toDocument(Capability capability) {
        return CapabilityDocument.builder()
                .id(capability.name())
                .build();
    }
}
