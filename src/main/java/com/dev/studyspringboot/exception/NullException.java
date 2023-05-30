package com.dev.studyspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullException extends RuntimeException{
    public NullException(String message) {
        super(message);
    }
}
