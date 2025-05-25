package com.tfg.auth.infrastructure.adapters.persistence.adapter;

import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.infrastructure.adapters.persistence.document.CapabilityDocument;
import com.tfg.auth.infrastructure.adapters.persistence.repository.CapabilityRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CapabilityRepositoryAdapterIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private CapabilityRepositoryAdapter adapter;

    @Autowired
    private CapabilityRepository capabilityRepository;

    @BeforeEach
    void setUp() {
        capabilityRepository.deleteAll();

        CapabilityDocument cap = CapabilityDocument.builder()
                .id("VIEW_USERS")
                .build();

        capabilityRepository.save(cap);
    }

    @Test
    @DisplayName("Should find capability by id")
    void shouldFindById() {
        Optional<Capability> result = adapter.findById("VIEW_USERS");
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("VIEW_USERS");
    }

    @Test
    @DisplayName("Should find all capabilities")
    void shouldFindAll() {
        List<Capability> capabilities = adapter.findAll();
        assertThat(capabilities).isNotEmpty();
        assertThat(capabilities).extracting(Capability::name).contains("VIEW_USERS");
    }

    @Test
    @DisplayName("Should save capability")
    void shouldSave() {
        Capability cap = Capability.CREATE_USER;
        adapter.save(cap);

        Optional<CapabilityDocument> doc = capabilityRepository.findById("CREATE_USER");
        assertThat(doc).isPresent();
        assertThat(doc.get().getId()).isEqualTo("CREATE_USER");
    }

    @Test
    @DisplayName("Should check if capability exists")
    void shouldCheckExistsById() {
        assertThat(adapter.existsById("VIEW_USERS")).isTrue();
        assertThat(adapter.existsById("UNKNOWN")).isFalse();
    }
}
