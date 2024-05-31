package com.code.auth.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  // 409 Conflict
    @ResponseBody
    public String handleEmailExistsException(EmailExistsException e) {
        return e.getMessage();
    }
    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String handleUsernameExistsException(UsernameExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(value = { AuthenticationException.class })
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication error: " + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Authentication failed: " + e.getMessage()));
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Error occurred: " + e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An error occurred: " + e.getMessage()));
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    // You can add more handlers for different types of exceptions here
}
