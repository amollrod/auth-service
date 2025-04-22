package com.tfg.auth.domain.ports;

import com.tfg.auth.domain.models.AppUser;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    AppUser save(AppUser appUser);
}
