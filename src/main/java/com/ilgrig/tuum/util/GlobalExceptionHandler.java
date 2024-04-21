package com.ilgrig.tuum.util;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        if (ex.getCause() instanceof InvalidFormatException ife) {
            return handleInvalidFormatException(ife);
        }
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ife) {
        String path = ife.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
        String message = String.format("Invalid input for field [%s]: '%s' is not valid. Expected values: %s",
                path, ife.getValue(), Arrays.toString(ife.getTargetType().getEnumConstants()));

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, "JSON Parse Error", message);
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(HttpStatus.CONFLICT, "Insufficient Funds", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(BalanceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBalanceNotFoundException(BalanceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Unhandled exception caught: ", ex);
        String detailedMessage = constructDetailedErrorMessage(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", detailedMessage));
    }

    private String constructDetailedErrorMessage(Exception ex) {
        String message = String.format("Error of type %s occurred with message: %s", ex.getClass().getSimpleName(), ex.getLocalizedMessage());
        if (isDebugModeEnabled()) {
            String stackTrace = getStackTraceAsString(ex);
            message += String.format(". Stack Trace: %s", stackTrace);
        }
        return message;
    }

    private boolean isDebugModeEnabled() {
        return true;
    }

    private String getStackTraceAsString(Exception ex) {
        return org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex);
    }
}
