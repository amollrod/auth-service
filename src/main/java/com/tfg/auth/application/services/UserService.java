package com.tfg.auth.application.services;

import com.tfg.auth.application.dto.CreateUserRequest;
import com.tfg.auth.application.dto.UpdateUserRequest;
import com.tfg.auth.application.dto.UserResponse;
import com.tfg.auth.application.mapper.UserMapper;
import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.services.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainService userDomainService;

    public UserResponse createUser(CreateUserRequest request) {
        AppUser user = userDomainService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getRoles()
        );
        return UserMapper.toResponse(user);
    }

    public UserResponse updateUser(String email, UpdateUserRequest request) {
        AppUser updated = userDomainService.updateUser(
                email,
                request.getPassword(),
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
                .collect(Collectors.toList());
    }

    public UserResponse getUser(String email) {
        return UserMapper.toResponse(userDomainService.getUser(email));
    }
}
