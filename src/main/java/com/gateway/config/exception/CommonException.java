package com.gateway.config.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CommonExceptionType type;

    public CommonException(CommonExceptionType type) {
        super(type.getErrorMessage());
        this.type = type;
    }

    public CommonException(CommonExceptionType type, String message) {
        super(type.getErrorMessage() + message);
        this.type = type;
    }

}
