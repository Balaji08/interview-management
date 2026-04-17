package com.project.interviewmanagement_service.common.exception;

import com.project.interviewmanagement_service.common.dto.ApiResponse;
import com.project.interviewmanagement_service.common.utils.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Centralized exception handler for the application.
 * Handles all exceptions and returns consistent API responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles unexpected system-level exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {

        return ResponseEntity.internalServerError()
                .body(ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR));
    }


    /**
     * Handles resource not found scenarios.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
    }

    /**
     * Handles database constraint violations (e.g., unique key conflicts).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(ErrorCode.DATA_INTEGRITY_VIOLATION));
    }

    /**
     * Handles validation errors triggered by @Valid annotation.
     * Returns field-wise validation messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(ErrorCode.VALIDATION_ERROR, ErrorCode.VALIDATION_ERROR.getMessage(), errors));
    }



    /**
     * Handles custom business and domain exceptions.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(ex.getErrorCode()));

    }

    /**
     * Handles validation-related custom exceptions.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomValidation(ValidationException ex) {

        return ResponseEntity.badRequest().body(
              ApiResponse.failure(ex.getErrorCode(), ex.getMessage())
        );
    }

}
