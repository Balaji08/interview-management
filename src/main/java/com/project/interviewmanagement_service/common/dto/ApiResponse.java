package com.project.interviewmanagement_service.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.interviewmanagement_service.common.utils.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorCode errorCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, null, data);
    }


    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null, null);
    }


    public static <T> ApiResponse<T> failure(ErrorCode errorCode, String message) {
        return new ApiResponse<>(false, message, errorCode, null);
    }
    public static <T> ApiResponse<T> failure(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getMessage(), errorCode, null);
    }

    public static <T> ApiResponse<T> failure(ErrorCode errorCode, String message, T data) {
        return new ApiResponse<>(false, message, errorCode, data);
    }
}
