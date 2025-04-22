package com.tfg.auth.application.dto;

import java.util.Set;

public record UserMeResponse(
        String email,
        Set<String> roles,
        Set<String> capabilities
) {}