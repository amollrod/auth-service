package com.tfg.auth.infrastructure.config;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.infrastructure.adapters.persistence.document.CapabilityDocument;
import com.tfg.auth.infrastructure.adapters.persistence.document.RoleDocument;
import com.tfg.auth.infrastructure.adapters.persistence.document.UserDocument;
import com.tfg.auth.infrastructure.adapters.persistence.repository.CapabilityRepository;
import com.tfg.auth.infrastructure.adapters.persistence.repository.RoleRepository;
import com.tfg.auth.infrastructure.adapters.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * This class is responsible for seeding the database with initial data when the application starts.
 * It creates capabilities, roles, and an admin user if they do not already exist in the database.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final CapabilityRepository capabilityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_ROLE_NAME = "ADMIN_ROLE";
    private final String USER_ROLE_NAME = "USER_ROLE";
    private final String ADMIN_USER_NAME = "admin";
    private final String ADMIN_USER_PASSWORD = "admin123";

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
                capabilityRepository.save(CapabilityDocument.builder().id(cap.name()).build());
            }
        });
    }

    /**
     * This method is used to create roles in the database if they do not already exist.
     * It creates two roles: ROLE_ADMIN and ROLE_USER with their respective capabilities.
     */
    private void createRolesIfNotExists() {
        if (!roleRepository.existsById(ADMIN_ROLE_NAME)) {
            roleRepository.save(RoleDocument.builder()
                    .name(ADMIN_ROLE_NAME)
                    .capabilities(Set.of(
                            Capability.CREATE_PACKAGE.name(),
                            Capability.UPDATE_STATUS.name()
                    ))
                    .build());
        }
        if (!roleRepository.existsById(USER_ROLE_NAME)) {
            roleRepository.save(RoleDocument.builder()
                    .name(USER_ROLE_NAME)
                    .capabilities(Set.of(
                            Capability.FIND_PACKAGE.name(),
                            Capability.SEARCH_PACKAGES.name(),
                            Capability.VIEW_HISTORY.name()
                    ))
                    .build());
        }
    }

    /**
     * This method is used to create an initial admin user in the database if it does not already exist.
     * The admin user has the username ADMIN_USER_NAME and password ADMIN_USER_PASSWORD.
     */
    private void createAdminUserIfNotExists() {
        if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
            userRepository.save(UserDocument.builder()
                    .email(ADMIN_USER_NAME)
                    .password(passwordEncoder.encode(ADMIN_USER_PASSWORD))
                    .enabled(true)
                    .roleNames(Set.of(ADMIN_ROLE_NAME))
                    .build());
        }
    }
}