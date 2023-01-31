package com.ecore.roles.web.rest;

import com.ecore.roles.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException exception) {
        return createResponse(400, "Database integrity violation: " + exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(InvalidArgumentException exception) {
        return createResponse(400, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ResourceNotFoundException exception) {
        return createResponse(404, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ResourceExistsException exception) {
        return createResponse(400, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(IllegalStateException exception) {
        return createResponse(500, exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        return createResponse(400, exception);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException exception) {
        return createResponse(400, exception);
    }

    private ResponseEntity<ErrorResponse> createResponse(int status, String exception) {
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .error(exception).build());
    }

    private ResponseEntity<ErrorResponse> createResponse(
            int status,
            MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .error("Bad Request")
                        .violations(exception.getBindingResult().getFieldErrors().stream()
                                .map(p -> ErrorDetail.builder()
                                        .field(p.getField())
                                        .message(p.getDefaultMessage())
                                        .build())
                                .toArray(ErrorDetail[]::new))
                        .build());
    }

    private ResponseEntity<ErrorResponse> createResponse(int status, ConstraintViolationException exception) {
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .error("Bad Request")
                        .violations(exception.getConstraintViolations().stream()
                                .map(p -> ErrorDetail.builder()
                                        .field(p.getPropertyPath().toString())
                                        .message(p.getMessage())
                                        .build())
                                .toArray(ErrorDetail[]::new))
                        .build());
    }
}
