package com.tfg.auth.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private boolean enabled;
    private Set<Role> roles;

    public boolean hasCapability(Capability capability) {
        return roles.stream()
                .flatMap(role -> role.getCapabilities().stream())
                .anyMatch(cap -> cap == capability);
    }
}
