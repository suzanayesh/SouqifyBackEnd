package com.code.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class for handling not found errors.
 * When thrown, this will cause Spring MVC to return a 404 Not Found status.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor for ResourceNotFoundException.
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
