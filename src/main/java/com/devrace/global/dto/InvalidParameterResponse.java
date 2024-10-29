package com.devrace.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class InvalidParameterResponse {

    private String field;
    private String message;

    @Builder
    private InvalidParameterResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public static InvalidParameterResponse of(FieldError e) {
        return InvalidParameterResponse.builder()
                .field(e.getField())
                .message(e.getDefaultMessage())
                .build();
    }
}
