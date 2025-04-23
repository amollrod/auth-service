package com.tfg.auth.domain.exceptions;

public class UserAlreadyExistsException extends AuthDomainException {
    public UserAlreadyExistsException(String email) {
        super("User already exists: " + email);
    }
}
