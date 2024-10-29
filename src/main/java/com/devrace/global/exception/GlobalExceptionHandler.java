package com.devrace.global.exception;


import static com.devrace.global.exception.ErrorCode.ILLEGAL_ARGUMENT_ERROR;

import com.devrace.global.dto.ExceptionResponse;
import com.devrace.global.dto.InvalidParameterResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse<List<InvalidParameterResponse>>> methodArgNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        List<InvalidParameterResponse> invalidParameterResponses = errors.stream()
                .map(InvalidParameterResponse::of)
                .toList();
        
        return ResponseEntity.status(ILLEGAL_ARGUMENT_ERROR.getHttpStatus()).body(
                ExceptionResponse.of(
                        ILLEGAL_ARGUMENT_ERROR.getHttpStatus(),
                        ILLEGAL_ARGUMENT_ERROR.getMessage(),
                        invalidParameterResponses));
    }

    /**
     * [Exception] CustomException 반환 ErrorCode에 작성된 예외를 반환하는 경우 사용
     *
     * @param e CustomException
     * @return ResponseEntity<ExceptionResponse>
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse<Void>> customExceptionHandler(CustomException e) {
        log.error("CustomException: " + e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(
                ExceptionResponse.of(
                        e.getErrorCode().getHttpStatus(),
                        e.getErrorCode().getMessage(),
                        null
                )
        );
    }

    /**
     * [Exception] RuntimeException 반환
     *
     * @param e RuntimeException
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
}
