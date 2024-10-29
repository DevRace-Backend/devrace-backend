package com.devrace.domain.log.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LogResponseDto {

    private final Long logId;
    private final String address;
    private final LocalDateTime createdAt;

}
