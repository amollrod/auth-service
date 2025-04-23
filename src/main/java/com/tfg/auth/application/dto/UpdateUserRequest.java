package com.tfg.auth.application.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    private String password;
    private boolean enabled;
    private Set<String> roles;
}
