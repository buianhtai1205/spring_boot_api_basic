package com.dev.studyspringboot.exception;

import com.dev.studyspringboot.dto.DefaultResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullException.class)
    public ResponseEntity<DefaultResponse> handleNullException(NullException ex) {
        DefaultResponse defaultResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(defaultResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DefaultResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        DefaultResponse defaultResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(defaultResponse);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<DefaultResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        DefaultResponse defaultResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(defaultResponse);
    }

    @ExceptionHandler(WarningException.class)
    public ResponseEntity<DefaultResponse> handleWarningException(WarningException ex) {
        DefaultResponse defaultResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(defaultResponse);
    }

    // Handle BadCredentialsException return DefaultResponse
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DefaultResponse> handleBadCredentialsException(BadCredentialsException ex) {
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    //
    @ExceptionHandler(BindException.class)
    public ResponseEntity<DefaultResponse> handleBindException(BindException ex) {
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // Handle others exception not define
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse> handleException(Exception ex) {
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(500)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
