package com.tfg.auth.domain.services;

import com.tfg.auth.domain.exceptions.RoleNotFoundException;
import com.tfg.auth.domain.exceptions.UserAlreadyExistsException;
import com.tfg.auth.domain.exceptions.UserNotFoundException;
import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.RoleRepositoryPort;
import com.tfg.auth.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDomainService {

    private final UserRepositoryPort userRepo;
    private final RoleRepositoryPort roleRepo;
    private final PasswordEncoder encoder;

    /**
     * Creates a new user with the given email, password, and roles.
     *
     * @param email      the email of the user
     * @param rawPassword the raw password of the user
     * @param roleNames  the set of role names to assign to the user
     * @return the created AppUser
     */
    public AppUser createUser(String email, String rawPassword, Set<String> roleNames) {
        if (userRepo.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        Set<Role> roles = resolveRoles(roleNames);

        AppUser user = AppUser.builder()
                .email(email)
                .password(encoder.encode(rawPassword))
                .roles(roles)
                .enabled(true)
                .build();

        return userRepo.save(user);
    }

    /**
     * Updates the user with the given email, setting the new password, enabled status, and roles.
     *
     * @param email      the email of the user to update
     * @param newPassword the new password for the user
     * @param enabled    whether the user should be enabled or disabled
     * @param roleNames  the set of role names to assign to the user
     * @return the updated AppUser
     */
    public AppUser updateUser(String email, String newPassword, boolean enabled, Set<String> roleNames) {
        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(encoder.encode(newPassword));
        }

        if (enabled) user.enable(); else user.disable();

        user.assignRoles(resolveRoles(roleNames));

        return userRepo.save(user);
    }

    /**
     * Deletes the user with the given email.
     *
     * @param email the email of the user to delete
     */
    public void deleteUser(String email) {
        if (!userRepo.existsByEmail(email)) {
            throw new UserNotFoundException(email);
        }
        userRepo.deleteByEmail(email);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all AppUser
     */
    public List<AppUser> getAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Retrieves the user with the given email.
     *
     * @param email the email of the user to retrieve
     * @return the AppUser with the given email
     */
    public AppUser getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    /**
     * Resolves the roles from the given set of role names.
     *
     * @param roleNames the set of role names to resolve
     * @return a set of Role
     */
    private Set<Role> resolveRoles(Set<String> roleNames) {
        return roleNames.stream()
                .map(name -> roleRepo.findByName(name)
                        .orElseThrow(() -> new RoleNotFoundException(name)))
                .collect(Collectors.toSet());
    }
}
