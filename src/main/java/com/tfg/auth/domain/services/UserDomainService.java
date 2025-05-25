package com.tfg.auth.domain.services;

import com.tfg.auth.domain.exceptions.RoleNotFoundException;
import com.tfg.auth.domain.exceptions.UserAlreadyExistsException;
import com.tfg.auth.domain.exceptions.UserNotFoundException;
import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import com.tfg.auth.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepositoryPort userRepo;
    private final RoleRepositoryPort roleRepo;

    public AppUser createUser(String email, String encodedPassword, Set<String> roleNames) {
        if (userRepo.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        Set<Role> roles = resolveRoles(roleNames);

        AppUser user = AppUser.builder()
                .email(email)
                .password(encodedPassword)
                .roles(roles)
                .enabled(true)
                .build();

        return userRepo.save(user);
    }

    public AppUser updateUser(String email, String encodedPassword, boolean enabled, Set<String> roleNames) {
        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (encodedPassword != null && !encodedPassword.isBlank()) {
            user.setPassword(encodedPassword);
        }

        if (enabled) user.enable(); else user.disable();

        user.assignRoles(resolveRoles(roleNames));

        return userRepo.save(user);
    }

    public void deleteUser(String email) {
        if (!userRepo.existsByEmail(email)) {
            throw new UserNotFoundException(email);
        }
        userRepo.deleteByEmail(email);
    }

    public List<AppUser> getAllUsers() {
        return userRepo.findAll();
    }

    public AppUser getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        return roleNames.stream()
                .map(name -> roleRepo.findByName(name)
                        .orElseThrow(() -> new RoleNotFoundException(name)))
                .collect(Collectors.toSet());
    }
}
