package com.tfg.auth.infrastructure.adapters.persistence.adapter;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.infrastructure.adapters.persistence.document.RoleDocument;
import com.tfg.auth.infrastructure.adapters.persistence.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoleRepositoryAdapterIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private RoleRepositoryAdapter adapter;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();

        RoleDocument role = RoleDocument.builder()
                .name("ADMIN")
                .capabilities(Set.of(Capability.VIEW_USERS.name(), Capability.CREATE_USER.name()))
                .build();

        roleRepository.save(role);
    }

    @Test
    @DisplayName("Should find role by name")
    void shouldFindRoleByName() {
        Optional<Role> result = adapter.findByName("ADMIN");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("ADMIN");
        assertThat(result.get().getCapabilities()).contains(Capability.VIEW_USERS, Capability.CREATE_USER);
    }

    @Test
    @DisplayName("Should return all roles")
    void shouldFindAllRoles() {
        List<Role> roles = adapter.findAll();
        assertThat(roles).hasSize(1);
    }

    @Test
    @DisplayName("Should save new role")
    void shouldSaveRole() {
        Role role = Role.builder()
                .name("MODERATOR")
                .capabilities(Set.of(Capability.VIEW_USERS))
                .build();

        adapter.save(role);

        Optional<RoleDocument> saved = roleRepository.findById("MODERATOR");
        assertThat(saved).isPresent();
        assertThat(saved.get().getCapabilities()).containsExactly(Capability.VIEW_USERS.name());
    }

    @Test
    @DisplayName("Should check if role exists")
    void shouldCheckIfRoleExists() {
        assertThat(adapter.existsByName("ADMIN")).isTrue();
        assertThat(adapter.existsByName("UNKNOWN")).isFalse();
    }

    @Test
    @DisplayName("Should delete role by name")
    void shouldDeleteRoleByName() {
        adapter.deleteByName("ADMIN");
        assertThat(adapter.existsByName("ADMIN")).isFalse();
    }
}
