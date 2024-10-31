package com.devrace.domain.algorithm.solution.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class AlgorithmResponseDto {
    private final Long solutionId;
    private final String title;
    private final ZonedDateTime createdAt;
    private final String description;
    private final String review;
    private final boolean isPublic;
    private final String nickName;
    private final String imageUrl;
//    private final String levelBadgeImageUrl;

}
