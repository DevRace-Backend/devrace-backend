package com.devrace.domain.algorithm.solution.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditAlgorithmDto {
    private String address;
    private String title;
    private String description;
    private String review;
    private boolean isPublic;
}
