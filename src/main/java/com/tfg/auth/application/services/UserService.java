package com.tfg.auth.application.services;

import com.tfg.auth.application.dto.CreateUserRequest;
import com.tfg.auth.application.dto.UpdateUserRequest;
import com.tfg.auth.application.dto.UserResponse;
import com.tfg.auth.application.mapper.UserMapper;
import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.services.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        AppUser user = userDomainService.createUser(
                request.getEmail(),
                encodedPassword,
                request.getRoles()
        );
        return UserMapper.toResponse(user);
    }

    public UserResponse updateUser(String email, UpdateUserRequest request) {
        String encodedPassword = request.getPassword() != null && !request.getPassword().isBlank()
                ? passwordEncoder.encode(request.getPassword())
                : null;
        AppUser updated = userDomainService.updateUser(
                email,
                encodedPassword,
                request.isEnabled(),
                request.getRoles()
        );
        return UserMapper.toResponse(updated);
    }

    public void deleteUser(String email) {
        userDomainService.deleteUser(email);
    }

    public List<UserResponse> getAllUsers() {
        return userDomainService.getAllUsers().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse getUser(String email) {
        return UserMapper.toResponse(userDomainService.getUser(email));
    }
}
