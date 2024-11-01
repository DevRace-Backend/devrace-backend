package com.devrace.domain.algorithm.comment.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class CommentResponseDto {

    private final Long id;
    private final String nickName;
    private final String profileImage;
//    private final String levelImage;
    private final String content;
    private final ZonedDateTime createdAt;

}
