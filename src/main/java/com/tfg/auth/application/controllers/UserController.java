package com.tfg.auth.application.controllers;

import com.tfg.auth.application.dto.CreateUserRequest;
import com.tfg.auth.application.dto.UpdateUserRequest;
import com.tfg.auth.application.dto.UserResponse;
import com.tfg.auth.application.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUser(email));
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String email,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(email, request));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
