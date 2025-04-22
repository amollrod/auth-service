package com.tfg.auth.domain.ports;

import com.tfg.auth.domain.models.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
}
