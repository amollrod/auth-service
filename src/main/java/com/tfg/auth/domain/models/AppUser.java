package com.tfg.auth.domain.models;

import lombok.*;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class AppUser {

    @NonNull
    private final String email;

    @Setter
    @NonNull
    private String password;

    @Setter
    private boolean enabled;

    @Setter
    @Builder.Default
    private Set<Role> roles = Set.of();

    /**
     * Checks if the user has a specific capability.
     */
    public boolean hasCapability(Capability capability) {
        return roles.stream().anyMatch(role -> role.hasCapability(capability));
    }

    /**
     * Checks if the user has a role with the given name.
     */
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    /**
     * Enables the user.
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * Disables the user.
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * Replaces all roles with the given set.
     */
    public void assignRoles(Set<Role> roles) {
        this.roles = roles != null ? roles : Set.of();
    }
}
