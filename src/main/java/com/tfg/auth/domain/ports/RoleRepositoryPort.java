package com.tfg.auth.domain.ports;

import com.tfg.auth.domain.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepositoryPort {
    Optional<Role> findByName(String name);
    List<Role> findAll();
    Role save(Role role);
    boolean existsByName(String name);
}
