package com.code.auth.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

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


    // You can add more handlers for different types of exceptions here
}
