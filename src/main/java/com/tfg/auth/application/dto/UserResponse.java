package com.tfg.auth.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private String email;
    private boolean enabled;
    private Set<String> roles;
}
