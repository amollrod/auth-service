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
public class AppUser {
    private String email;
    private String password;
    private boolean enabled;
    private Set<Role> roles;

    /**
     * Checks if the user has a specific capability.
     *
     * @param capability the capability to check
     * @return true if the user has the capability, false otherwise
     */
    public boolean hasCapability(Capability capability) {
        return roles.stream()
                .flatMap(role -> role.getCapabilities().stream())
                .anyMatch(cap -> cap == capability);
    }
}
