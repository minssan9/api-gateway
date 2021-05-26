package com.gateway.exceptionhandler;

import com.gateway.exception.CommonException;
import com.gateway.exception.CommonExceptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponse {

    private String errorCode;
    private String errorMessage;

    public ExceptionResponse(CommonExceptionType type) {
        this.errorCode = type.getErrorCode();
        this.errorMessage = type.getErrorMessage();
    }

    public ExceptionResponse(CommonExceptionType type, Exception e) {
        this.errorCode = type.getErrorCode();
        this.errorMessage = type.getErrorMessage() + ", " + e.getMessage();
    }

    public ExceptionResponse(CommonException e) {
        this.errorCode = e.getType().getErrorCode();
        this.errorMessage = e.getMessage();
    }

}
