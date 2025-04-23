package com.tfg.auth.domain.ports;

import com.tfg.auth.domain.models.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    List<AppUser> findAll();
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    AppUser save(AppUser appUser);
    void deleteByEmail(String email);
}
