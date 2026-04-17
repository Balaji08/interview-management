package com.project.interviewmanagement_service.common.exception;

import com.project.interviewmanagement_service.common.utils.ErrorCode;

/**
 * Base class for all custom application exceptions.
 * Encapsulates common error handling using ErrorCode.
 */
public abstract class BaseException extends  RuntimeException {

    private final ErrorCode errorCode;

    protected BaseException(ErrorCode errorCode, Object... args) {
        super(errorCode.format(args)); // 🔥 message comes from enum
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
