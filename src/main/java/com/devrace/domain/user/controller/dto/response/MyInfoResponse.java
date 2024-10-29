package com.devrace.domain.user.controller.dto.response;

import com.devrace.domain.user.entity.User;
import lombok.Getter;

@Getter
public class MyInfoResponse {

    private String imageUrl;
    private String nickname;
    private String description;
    private String blogAddress;
    private String githubName;

    public static MyInfoResponse from(User user) {
        return new MyInfoResponse(
                user.getImageUrl(),
                user.getNickname(),
                user.getDescription(),
                user.getBlogAddress(),
                user.getGithubName());
    }

    private MyInfoResponse(String imageUrl, String nickname, String description, String blogAddress, String githubName) {
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.description = description;
        this.blogAddress = blogAddress;
        this.githubName = githubName;
    }
}
