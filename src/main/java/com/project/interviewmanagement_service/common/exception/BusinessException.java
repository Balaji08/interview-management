package com.project.interviewmanagement_service.common.exception;

import com.project.interviewmanagement_service.common.utils.ErrorCode;

/**
 * Represents business rule violations.
 * Example: interview time conflict, invalid state transitions.
 */
public class BusinessException extends BaseException{

   public BusinessException(ErrorCode code, Object... args)
   {
       super(code,args);
   }
}
