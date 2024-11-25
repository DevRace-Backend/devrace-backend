package com.devrace.domain.log.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SubmitLogResponseDto {
    private final String status;
    private final String message;
    private final Long logId;
    private final ZonedDateTime createdAt;
    private final String address;
    private final boolean isPublic;
}
