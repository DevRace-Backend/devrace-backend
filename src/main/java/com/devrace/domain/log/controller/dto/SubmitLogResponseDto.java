package com.devrace.domain.log.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
public class SubmitLogResponseDto {
    private final String status;
    private final String message;
    private final Long logId;
    private final ZonedDateTime createdAt;
    private final String address;
}
