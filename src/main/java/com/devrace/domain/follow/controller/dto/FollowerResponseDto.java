package com.devrace.domain.follow.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowerResponseDto {
    private Long id;
    private String followerNickname;
    private String imageUrl;
    private Boolean isFollowing;
}
