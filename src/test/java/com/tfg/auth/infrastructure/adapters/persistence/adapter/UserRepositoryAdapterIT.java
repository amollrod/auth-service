package com.tfg.auth.infrastructure.adapters.persistence.adapter;

import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.infrastructure.adapters.persistence.document.RoleDocument;
import com.tfg.auth.infrastructure.adapters.persistence.document.UserDocument;
import com.tfg.auth.infrastructure.adapters.persistence.repository.RoleRepository;
import com.tfg.auth.infrastructure.adapters.persistence.repository.UserRepository;
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
class UserRepositoryAdapterIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private UserRepositoryAdapter adapter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        RoleDocument role = RoleDocument.builder()
                .name("ADMIN")
                .capabilities(Set.of(Capability.VIEW_USERS.name(), Capability.CREATE_USER.name()))
                .build();

        roleRepository.save(role);

        UserDocument user = UserDocument.builder()
                .email("admin@example.com")
                .password("encrypted-password")
                .enabled(true)
                .roleNames(Set.of("ADMIN"))
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        Optional<AppUser> result = adapter.findByEmail("admin@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("admin@example.com");
        assertThat(result.get().getRoles()).extracting(Role::getName).contains("ADMIN");
    }

    @Test
    @DisplayName("Should return true if user exists")
    void shouldCheckIfUserExists() {
        boolean exists = adapter.existsByEmail("admin@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false if user does not exist")
    void shouldReturnFalseIfUserDoesNotExist() {
        boolean exists = adapter.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should save and retrieve user")
    void shouldSaveUser() {
        Role adminRole = Role.builder()
                .name("ADMIN")
                .capabilities(Set.of(Capability.VIEW_USERS, Capability.CREATE_USER))
                .build();

        AppUser newUser = AppUser.builder()
                .email("new@example.com")
                .password("hashed-password")
                .enabled(true)
                .roles(Set.of(adminRole))
                .build();

        adapter.save(newUser);

        Optional<AppUser> result = adapter.findByEmail("new@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("new@example.com");
    }

    @Test
    @DisplayName("Should delete user by email")
    void shouldDeleteUserByEmail() {
        adapter.deleteByEmail("admin@example.com");
        assertThat(adapter.findByEmail("admin@example.com")).isNotPresent();
    }

    @Test
    @DisplayName("Should retrieve all users")
    void shouldFindAllUsers() {
        List<AppUser> users = adapter.findAll();
        assertThat(users).isNotEmpty();
    }
}
