package com.tfg.auth.application.dto;

import com.tfg.auth.domain.models.Capability;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoleResponse {
    private String name;
    private Set<Capability> capabilities;
}
