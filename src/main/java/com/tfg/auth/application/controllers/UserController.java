package com.tfg.auth.application.controllers;

import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.application.dto.UserMeResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/me")
public class UserController {

    @GetMapping
    public UserMeResponse getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
        Set<String> roles = appUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<String> capabilities = appUser.getRoles().stream()
                .flatMap(role -> role.getCapabilities().stream())
                .map(Capability::name)
                .collect(Collectors.toSet());

        return new UserMeResponse(appUser.getEmail(), roles, capabilities);
    }
}
