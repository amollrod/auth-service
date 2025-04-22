package com.tfg.auth.application.controllers;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.models.User;
import com.tfg.auth.application.dto.UserMeResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/me")
public class UserController {

    @GetMapping
    public UserMeResponse getCurrentUser(@AuthenticationPrincipal User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<String> capabilities = user.getRoles().stream()
                .flatMap(role -> role.getCapabilities().stream())
                .map(Capability::name)
                .collect(Collectors.toSet());

        return new UserMeResponse(user.getEmail(), roles, capabilities);
    }
}
