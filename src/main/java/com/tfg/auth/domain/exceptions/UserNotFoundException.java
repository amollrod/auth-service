package com.tfg.auth.domain.exceptions;

public class UserNotFoundException extends AuthDomainException {
    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}
