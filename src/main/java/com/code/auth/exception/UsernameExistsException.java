package com.code.auth.exception;


public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String name) {
        super("Username already exists: " + name);
    }
}
