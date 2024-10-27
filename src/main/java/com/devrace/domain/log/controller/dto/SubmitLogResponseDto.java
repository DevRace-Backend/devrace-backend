package com.devrace.domain.log.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubmitLogResponseDto {
    private final String status;
    private final String message;
    private final Long logId;
    private final LocalDateTime createdAt;
    private final String address;
}
