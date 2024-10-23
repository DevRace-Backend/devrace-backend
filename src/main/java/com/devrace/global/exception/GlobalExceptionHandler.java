package com.devrace.global.exception;


import com.devrace.global.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * [Exception] RuntimeException 반환
     *
     * @param e  RuntimeException
     * @return ResponseEntity<ExceptionResponse>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse<Void>> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException: ", e);
        return ResponseEntity.internalServerError().body(
                ExceptionResponse.of(
                        ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                        null)
        );
    }

    /**
     * [Exception] CustomException 반환
     * ErrorCode에 작성된 예외를 반환하는 경우 사용
     *
     * @param e  CustomException
     * @return ResponseEntity<ExceptionResponse>
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse<Void>> customExceptionHandler(CustomException e) {
        log.error("CustomException: "+ e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(
                ExceptionResponse.of(
                        e.getErrorCode().getHttpStatus(),
                        e.getErrorCode().getMessage(),
                        null
                )
        );
    }

}
