package com.tfg.auth.application.controllers;

import com.tfg.auth.application.dto.CreateRoleRequest;
import com.tfg.auth.application.dto.UpdateRoleRequest;
import com.tfg.auth.application.dto.RoleResponse;
import com.tfg.auth.application.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    @PutMapping("/{name}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable String name,
            @RequestBody @Valid UpdateRoleRequest request
    ) {
        return ResponseEntity.ok(roleService.updateRole(name, request));
    }

    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping("/{name}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable String name) {
        return ResponseEntity.ok(roleService.getRole(name));
    }

    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ResponseEntity.noContent().build();
    }
}
