package com.project.interviewmanagement_service.common.exception;

import com.project.interviewmanagement_service.common.utils.ErrorCode;

/**
 * Thrown when input validation fails.
 * Example: invalid date range, invalid status transition.
 */
public class ValidationException extends BaseException {

    public ValidationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
