package org.kiworkshop.blind.security;

public class UnAuthenticationException extends RuntimeException {
    public UnAuthenticationException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
