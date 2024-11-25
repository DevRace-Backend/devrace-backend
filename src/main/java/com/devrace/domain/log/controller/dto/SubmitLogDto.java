package com.devrace.domain.log.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmitLogDto {

    private String address;
    private String title;
    private String content;
    private boolean isPublic;

}
