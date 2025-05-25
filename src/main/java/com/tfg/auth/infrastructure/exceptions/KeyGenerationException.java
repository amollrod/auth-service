package com.tfg.auth.infrastructure.exceptions;

/**
 * Exception thrown when there is an error during RSA key generation.
 */
public class KeyGenerationException extends RuntimeException {
  public KeyGenerationException(Throwable cause) {
    super("Failed to generate RSA key pair", cause);
  }
}
