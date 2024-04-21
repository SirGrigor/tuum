package com.ilgrig.tuum.util;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status, String message) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", status.toString());
        errorDetails.put("message", message);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status.value());
        responseBody.put("error", errorDetails);

        return ResponseEntity.status(status).body(responseBody);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, InvalidFormatException.class})
    public ResponseEntity<Object> handleFormatException(Exception ex, WebRequest request) {
        String message = "Invalid input";
        if (ex instanceof InvalidFormatException) {
            String path = ((InvalidFormatException) ex).getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
            message = String.format("Invalid input for field [%s].", path);
        }
        logger.error("Format exception: {}", message, ex);
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.info("Validation error occurred: {}", errors);
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Validation Error: " + errors);
    }


    @ExceptionHandler({AccountNotFoundException.class, BalanceNotFoundException.class, InsufficientFundsException.class, AccountAlreadyExistsException.class, InvalidCurrencyException.class})
    public ResponseEntity<Object> handleBusinessExceptions(RuntimeException ex) {
        HttpStatus status = ex instanceof InsufficientFundsException ? HttpStatus.CONFLICT : HttpStatus.NOT_FOUND;
        logger.error("Business exception: {}", ex.getMessage(), ex);
        return buildErrorResponse(ex, status, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: ", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please review the logs for more details: " + ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.toList());
        String errorMessage = "Validation failed: " + String.join(", ", errors);
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, errorMessage);
    }


}
