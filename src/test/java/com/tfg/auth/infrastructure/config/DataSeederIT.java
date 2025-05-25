package com.tfg.auth.infrastructure.config;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.infrastructure.adapters.persistence.document.RoleDocument;
import com.tfg.auth.infrastructure.adapters.persistence.document.UserDocument;
import com.tfg.auth.infrastructure.adapters.persistence.repository.CapabilityRepository;
import com.tfg.auth.infrastructure.adapters.persistence.repository.RoleRepository;
import com.tfg.auth.infrastructure.adapters.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
class DataSeederIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private CapabilityRepository capabilityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String ADMIN_ROLE = "ADMIN_ROLE";
    private final String USER_ROLE = "USER_ROLE";
    private final String ADMIN_EMAIL = "admin@gmail.com";

    @Test
    @DisplayName("Should seed all capabilities")
    void shouldSeedAllCapabilities() {
        long count = capabilityRepository.count();
        assertThat(count).isEqualTo(Capability.values().length);
    }

    @Test
    @DisplayName("Should create ADMIN_ROLE with all capabilities")
    void shouldSeedAdminRole() {
        Optional<RoleDocument> role = roleRepository.findById(ADMIN_ROLE);
        assertThat(role).isPresent();
        assertThat(role.get().getCapabilities()).containsExactlyInAnyOrderElementsOf(
                Set.of(Capability.values()).stream().map(Enum::name).toList()
        );
    }

    @Test
    @DisplayName("Should create USER_ROLE with limited capabilities")
    void shouldSeedUserRole() {
        Optional<RoleDocument> role = roleRepository.findById(USER_ROLE);
        assertThat(role).isPresent();
        assertThat(role.get().getCapabilities()).containsExactlyInAnyOrder(
                Capability.FIND_PACKAGE.name(),
                Capability.SEARCH_PACKAGES.name(),
                Capability.VIEW_HISTORY.name()
        );
    }

    @Test
    @DisplayName("Should create initial admin user with ADMIN_ROLE")
    void shouldSeedAdminUser() {
        Optional<UserDocument> user = userRepository.findByEmail(ADMIN_EMAIL);
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo(ADMIN_EMAIL);
        assertThat(user.get().getRoleNames()).containsExactly(ADMIN_ROLE);
        assertThat(user.get().isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Admin password should be encoded")
    void shouldEncodeAdminPassword() {
        Optional<UserDocument> user = userRepository.findByEmail(ADMIN_EMAIL);
        assertThat(user).isPresent();
        assertThat(user.get().getPassword()).isNotEqualTo("amollrod");
        assertThat(passwordEncoder.matches("amollrod", user.get().getPassword())).isTrue();
    }
}
