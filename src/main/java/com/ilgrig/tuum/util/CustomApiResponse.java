package com.ilgrig.tuum.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomApiResponse<T> {
    private T data;
    private ApiErrorResponse error;

    public CustomApiResponse(T data) {
        this.data = data;
        this.error = null;
    }

    public CustomApiResponse(ApiErrorResponse error) {
        this.error = error;
        this.data = null;
    }

    public boolean hasError() {
        return error != null;
    }

    // Factory methods for success and error
    public static <T> CustomApiResponse<T> success(T data) {
        return new CustomApiResponse<>(data);
    }

    public static <T> CustomApiResponse<T> error(ApiErrorResponse error) {
        return new CustomApiResponse<>(null, error);
    }
}
