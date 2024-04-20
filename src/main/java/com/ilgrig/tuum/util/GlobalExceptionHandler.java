package com.ilgrig.tuum.util;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


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
                .body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getLocalizedMessage()));
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
