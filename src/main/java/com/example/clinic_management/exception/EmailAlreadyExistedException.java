package com.example.clinic_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistedException extends RuntimeException {

    public EmailAlreadyExistedException(String message) {
        super(message);
    }
}
