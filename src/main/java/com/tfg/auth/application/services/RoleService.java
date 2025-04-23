package com.tfg.auth.application.services;

import com.tfg.auth.application.dto.CreateRoleRequest;
import com.tfg.auth.application.dto.UpdateRoleRequest;
import com.tfg.auth.application.dto.RoleResponse;
import com.tfg.auth.application.mapper.RoleMapper;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.services.RoleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleDomainService roleDomainService;

    public RoleResponse createRole(CreateRoleRequest request) {
        Role role = roleDomainService.createRole(request.getName(), request.getCapabilities());
        return RoleMapper.toResponse(role);
    }

    public RoleResponse updateRole(String name, UpdateRoleRequest request) {
        Role updated = roleDomainService.updateRole(name, request.getCapabilities());
        return RoleMapper.toResponse(updated);
    }

    public RoleResponse getRole(String name) {
        return RoleMapper.toResponse(roleDomainService.getRole(name));
    }

    public List<RoleResponse> getAllRoles() {
        return roleDomainService.getAllRoles().stream()
                .map(RoleMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteRole(String name) {
        roleDomainService.deleteRole(name);
    }
}
