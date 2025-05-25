package com.tfg.auth.domain.services;

import com.tfg.auth.domain.exceptions.RoleAlreadyExistsException;
import com.tfg.auth.domain.exceptions.RoleNotFoundException;
import com.tfg.auth.domain.models.Capability;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class RoleDomainService {

    private final RoleRepositoryPort roleRepo;

    /**
     * Creates a new role with the given name and capabilities.
     *
     * @param name         the name of the new role
     * @param capabilities the capabilities to assign
     * @return the created Role
     */
    public Role createRole(String name, Set<Capability> capabilities) {
        if (roleRepo.existsByName(name)) {
            throw new RoleAlreadyExistsException(name);
        }

        Role role = Role.builder()
                .name(name)
                .capabilities(capabilities)
                .build();

        return roleRepo.save(role);
    }

    /**
     * Updates the capabilities of an existing role.
     *
     * @param name            the name of the role to update
     * @param newCapabilities the new capabilities to assign
     * @return the updated Role
     */
    public Role updateRole(String name, Set<Capability> newCapabilities) {
        Role role = roleRepo.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(name));

        role.setCapabilities(newCapabilities);

        return roleRepo.save(role);
    }

    /**
     * Adds a single capability to a role.
     *
     * @param roleName   the role to modify
     * @param capability the capability to add
     * @return the updated Role
     */
    public Role addCapabilityToRole(String roleName, Capability capability) {
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        role.addCapability(capability);

        return roleRepo.save(role);
    }

    /**
     * Removes a capability from a role.
     *
     * @param roleName   the role to modify
     * @param capability the capability to remove
     * @return the updated Role
     */
    public Role removeCapabilityFromRole(String roleName, Capability capability) {
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        role.removeCapability(capability);

        return roleRepo.save(role);
    }

    /**
     * Clears all capabilities from the role.
     *
     * @param roleName the role to clear
     * @return the updated Role
     */
    public Role clearCapabilities(String roleName) {
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        role.clearCapabilities();

        return roleRepo.save(role);
    }

    /**
     * Retrieves all roles in the system.
     *
     * @return a list of all roles
     */
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    /**
     * Retrieves a specific role by name.
     *
     * @param name the name of the role
     * @return the Role
     */
    public Role getRole(String name) {
        return roleRepo.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(name));
    }

    /**
     * Deletes the role with the given name.
     *
     * @param name the name of the role to delete
     */
    public void deleteRole(String name) {
        if (!roleRepo.existsByName(name)) {
            throw new RoleNotFoundException(name);
        }
        roleRepo.deleteByName(name);
    }
}
