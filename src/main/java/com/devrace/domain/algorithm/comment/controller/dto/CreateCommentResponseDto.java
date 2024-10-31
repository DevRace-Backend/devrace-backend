package com.devrace.domain.algorithm.comment.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class CreateCommentResponseDto {

    private final Long id;
    private final String nickName;
    private final String content;
    private final ZonedDateTime createdAt;

}
