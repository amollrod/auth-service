package com.tfg.auth.application.controllers;

import com.tfg.auth.application.dto.CreateRoleRequest;
import com.tfg.auth.application.dto.UpdateRoleRequest;
import com.tfg.auth.application.dto.RoleResponse;
import com.tfg.auth.application.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PutMapping("/{name}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable String name,
            @RequestBody @Valid UpdateRoleRequest request
    ) {
        return ResponseEntity.ok(roleService.updateRole(name, request));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{name}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable String name) {
        return ResponseEntity.ok(roleService.getRole(name));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ResponseEntity.noContent().build();
    }
}
