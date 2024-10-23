package com.devrace.global.exception;


import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String msg;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.msg = errorCode.getMessage();
    }
}
