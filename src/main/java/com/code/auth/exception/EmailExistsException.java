package com.code.auth.exception;


public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email) {
        super("Email already exists for user: " + email);
    }
}
