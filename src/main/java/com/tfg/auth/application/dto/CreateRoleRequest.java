package com.tfg.auth.application.dto;

import com.tfg.auth.domain.models.Capability;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CreateRoleRequest {
    @NotBlank
    private String name;
    private Set<Capability> capabilities;
}
