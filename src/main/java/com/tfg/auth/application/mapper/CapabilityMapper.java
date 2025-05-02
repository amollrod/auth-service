package com.tfg.auth.application.mapper;

import com.tfg.auth.application.dto.CapabilityResponse;
import com.tfg.auth.domain.models.Capability;

public class CapabilityMapper {
    public static CapabilityResponse toResponse(Capability capability) {
        return CapabilityResponse.builder()
                .name(capability.name())
                .description(capability.description())
                .build();
    }
}
