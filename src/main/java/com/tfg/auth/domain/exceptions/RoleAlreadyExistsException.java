package com.tfg.auth.domain.exceptions;

public class RoleAlreadyExistsException extends AuthDomainException {
    public RoleAlreadyExistsException(String name) {
        super("Role already exists: " + name);
    }
}
