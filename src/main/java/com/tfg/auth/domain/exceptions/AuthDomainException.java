package com.tfg.auth.domain.exceptions;

/**
 * Base class for all domain-level auth exceptions.
 */
public abstract class AuthDomainException extends RuntimeException {
    protected AuthDomainException(String message) {
        super(message);
    }
}

