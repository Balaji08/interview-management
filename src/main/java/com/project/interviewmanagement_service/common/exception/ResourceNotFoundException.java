package com.project.interviewmanagement_service.common.exception;

import com.project.interviewmanagement_service.common.utils.ErrorCode;
/**
 * Thrown when a requested resource is not found.
 * Example: candidate, interviewer, or interview not present.
 */
public class ResourceNotFoundException extends  BaseException{
    public ResourceNotFoundException(ErrorCode code, Object... args)
    {
        super(code, args);
    }
}
