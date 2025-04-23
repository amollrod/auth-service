package com.tfg.auth.application.mapper;

import com.tfg.auth.application.dto.RoleResponse;
import com.tfg.auth.domain.models.Role;

public class RoleMapper {

    public static RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .name(role.getName())
                .capabilities(role.getCapabilities())
                .build();
    }
}
