package com.gateway.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.SQLException;

@Getter
@AllArgsConstructor
public enum CommonExceptionType {
    INTERNAL_SERVER_ERROR("1000", "Internal server error"),

    // validation
    INVALID_PARAMS("2000", "Invalid params"),

    // database
    CANNOT_FOUND_USER("3000", "Cannot found user"),
    NOTEXIST_ACCOUNT_EXCEPTION_MSG("3000",  "계정이 없습니다."),
    SIGNIN_EXCEPTION_MSG("3000",  "로그인정보가 일치하지 않습니다."),
    EMAIL_EXIST_EXCEPTION_MSG("3000",  "이미 계정이 존재합니다."),
    NICKNAME_EXIST_EXCEPTION_MSG("3000",  "이미 닉네임이 존재합니다."),
    NONEXIST_DATA_EXCEPTION_MSG("3000",  "데이터를 찾을 수 없습니다. "),

    // business
    NOTEXIST_ITEM("4100", "Cannot found Item"),
    NOTEXIST_ITEM_TEMPLATE("4100", "Cannot found Item template"),

    // runtime
    NULL_POINTER_EXCEPTION("5100", "Null Pointer Exception"),

    // token
    TOKEN_EXPIRED("9000", "Access Token is expired"),
    TOKEN_MALFORMED("9000", "Access Token is Malformed"),

    UNAUTHORIZED_HOST("9000", "UnAuthorized Host");

    private final String errorCode;
    private final String errorMessage;
}
