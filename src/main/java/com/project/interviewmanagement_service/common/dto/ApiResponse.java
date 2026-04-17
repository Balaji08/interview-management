package com.project.interviewmanagement_service.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.interviewmanagement_service.common.utils.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Standard API response wrapper")
public class ApiResponse<T> {

    @Schema(description = "Indicates if the request was successful", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "Operation successful")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(
            description = "Error code (present only when success = false)",
            example = "null",
            nullable = true
    )
    private ErrorCode errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Response data (present only when success = true)")
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