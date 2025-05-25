package com.tfg.auth.application.config;

import com.tfg.auth.domain.ports.CapabilityRepositoryPort;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import com.tfg.auth.domain.ports.UserRepositoryPort;
import com.tfg.auth.domain.services.CapabilityDomainService;
import com.tfg.auth.domain.services.RoleDomainService;
import com.tfg.auth.domain.services.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides beans for domain services to keep domain layer framework-agnostic.
 */
@Configuration
public class DomainServiceConfig {

    /**
     * Creates a UserDomainService bean with the necessary ports.
     *
     * @param userRepo the user repository port
     * @param roleRepo the role repository port
     * @return a configured UserDomainService instance
     */
    @Bean
    public UserDomainService userDomainService(
        UserRepositoryPort userRepo,
        RoleRepositoryPort roleRepo
    ) {
        return new UserDomainService(userRepo, roleRepo);
    }

    /**
     * Creates a RoleDomainService bean with the necessary ports.
     *
     * @param roleRepo the role repository port
     * @return a configured RoleDomainService instance
     */
    @Bean
    public RoleDomainService roleDomainService(
        RoleRepositoryPort roleRepo
    ) {
        return new RoleDomainService(roleRepo);
    }

    /**
     * Creates a CapabilityDomainService bean with the necessary ports.
     *
     * @param capabilityRepo the capability repository port
     * @return a configured CapabilityDomainService instance
     */
    @Bean
    public CapabilityDomainService capabilityDomainService(
        CapabilityRepositoryPort capabilityRepo
    ) {
        return new CapabilityDomainService(capabilityRepo);
    }
}
