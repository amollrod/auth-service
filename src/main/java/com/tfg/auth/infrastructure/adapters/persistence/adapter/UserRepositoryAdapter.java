package com.tfg.auth.infrastructure.adapters.persistence.adapter;

import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.UserRepositoryPort;
import com.tfg.auth.infrastructure.adapters.persistence.mapper.RoleMapper;
import com.tfg.auth.infrastructure.adapters.persistence.mapper.UserMapper;
import com.tfg.auth.infrastructure.adapters.persistence.repository.RoleRepository;
import com.tfg.auth.infrastructure.adapters.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email).map(doc -> {
            List<Role> roles = doc.getRoleNames().stream()
                    .map(roleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(RoleMapper::toDomain)
                    .collect(Collectors.toList());
            return UserMapper.toDomain(doc, roles);
        });
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public AppUser save(AppUser appUser) {
        return UserMapper.toDomain(
                userRepository.save(UserMapper.toDocument(appUser)),
                List.copyOf(appUser.getRoles())
        );
    }
}
