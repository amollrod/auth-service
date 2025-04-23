package com.tfg.auth.application.mapper;

import com.tfg.auth.application.dto.UserResponse;
import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse toResponse(AppUser user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(roleNames)
                .build();
    }
}
