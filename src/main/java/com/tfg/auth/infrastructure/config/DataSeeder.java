package com.tfg.auth.infrastructure.config;

import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.CapabilityRepositoryPort;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import com.tfg.auth.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * This class is responsible for seeding the database with initial data when the application starts.
 * It creates capabilities, roles, and an admin user if they do not already exist in the database.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final CapabilityRepositoryPort capabilityRepository;
    private final RoleRepositoryPort roleRepository;
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_ROLE_NAME = "ADMIN_ROLE";
    private final String USER_ROLE_NAME = "USER_ROLE";
    private final String ADMIN_USER_EMAIL = "admin@gmail.com";
    private final String ADMIN_USER_PASSWORD = "admin";

    @EventListener(ApplicationReadyEvent.class)
    public void seedData() {
        createCapabilitiesIfNotExists();
        createRolesIfNotExists();
        createAdminUserIfNotExists();
    }

    /**
     * This method is used to create capabilities in the database if they do not already exist.
     * It iterates through all the values of the Capability enum and saves them to the database.
     */
    private void createCapabilitiesIfNotExists() {
        Arrays.stream(Capability.values()).forEach(cap -> {
            if (!capabilityRepository.existsById(cap.name())) {
                capabilityRepository.save(cap);
            }
        });
    }

    /**
     * This method is used to create roles in the database if they do not already exist.
     * It creates two roles: ROLE_ADMIN and ROLE_USER with their respective capabilities.
     */
    private void createRolesIfNotExists() {
        if (!roleRepository.existsByName(ADMIN_ROLE_NAME)) {
            roleRepository.save(Role.builder()
                    .name(ADMIN_ROLE_NAME)
                    .capabilities(Set.of(Capability.values()))
                    .build());
        }

        if (!roleRepository.existsByName(USER_ROLE_NAME)) {
            roleRepository.save(Role.builder()
                    .name(USER_ROLE_NAME)
                    .capabilities(Set.of(
                            Capability.FIND_PACKAGE,
                            Capability.SEARCH_PACKAGES,
                            Capability.VIEW_HISTORY
                    ))
                    .build());
        }
    }

    /**
     * This method is used to create an initial admin user in the database if it does not already exist.
     * The admin user has the username ADMIN_USER_NAME and password ADMIN_USER_PASSWORD.
     */
    private void createAdminUserIfNotExists() {
        if (!userRepository.existsByEmail(ADMIN_USER_EMAIL)) {
            Optional<Role> adminRole = roleRepository.findByName(ADMIN_ROLE_NAME);
            if (adminRole.isEmpty()) return;

            userRepository.save(AppUser.builder()
                    .email(ADMIN_USER_EMAIL)
                    .password(passwordEncoder.encode(ADMIN_USER_PASSWORD))
                    .enabled(true)
                    .roles(Set.of(adminRole.get()))
                    .build());
        }
    }
}
