package com.ilgrig.tuum.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;
    private String path;
}
