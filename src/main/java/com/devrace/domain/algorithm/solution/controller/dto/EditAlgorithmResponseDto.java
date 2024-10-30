package com.devrace.domain.algorithm.solution.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditAlgorithmResponseDto {
    private final Long id;
    private final String nickName;
    private final String address;
    private final String title;
    private final String description;
    private final String review;
    private final boolean isPublic;
}
