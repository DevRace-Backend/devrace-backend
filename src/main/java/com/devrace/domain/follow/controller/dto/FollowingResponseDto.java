package com.devrace.domain.follow.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowingResponseDto {
    private Long id;
    private String followingNickname;
    private String imageUrl;
    private String description;
    private Boolean isFollowing;
}
