package com.devrace.domain.user.controller.dto.response;

import com.devrace.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserInfoResponse {

    private final static String GITHUB_ADDRESS_PREFIX = "https://github.com/";

    private String nickname;
    private String description;

    // todo: 팔로잉, 팔로워 수 추가 필요

    private String blogAddress;
    private String githubAddress;

    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getNickname(),
                user.getDescription(),
                user.getBlogAddress(),
                user.getGithubName());
    }

    public UserInfoResponse(String nickname, String description, String blogAddress, String githubAddress) {
        this.nickname = nickname;
        this.description = description;
        this.blogAddress = blogAddress;
        this.githubAddress = githubAddress == null ? null : GITHUB_ADDRESS_PREFIX.concat(githubAddress);
    }
}
