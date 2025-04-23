package com.tfg.auth.domain.exceptions;

public class RoleNotFoundException extends AuthDomainException {
    public RoleNotFoundException(String name) {
        super("Role not found: " + name);
    }
}
