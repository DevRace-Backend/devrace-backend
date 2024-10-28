package com.devrace.domain.log.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmitLogDto {

    private final String address;
    private final String title;
    private final String content;

}
