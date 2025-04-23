package com.tfg.auth.infrastructure.adapters.persistence.adapter;

import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import com.tfg.auth.infrastructure.adapters.persistence.mapper.RoleMapper;
import com.tfg.auth.infrastructure.adapters.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findById(name).map(RoleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Role save(Role role) {
        return RoleMapper.toDomain(roleRepository.save(RoleMapper.toDocument(role)));
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsById(name);
    }

    @Override
    public void deleteByName(String name) {
        roleRepository.deleteById(name);
    }
}
