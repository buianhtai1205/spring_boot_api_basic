package com.dev.studyspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WarningException extends RuntimeException{
    public WarningException(String message) {
        super(message);
    }
}
