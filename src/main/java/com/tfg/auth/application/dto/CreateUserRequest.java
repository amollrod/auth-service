package com.tfg.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CreateUserRequest {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private Set<String> roles;
}
