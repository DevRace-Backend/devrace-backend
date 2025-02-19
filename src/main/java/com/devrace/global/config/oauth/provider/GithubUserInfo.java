package com.devrace.global.config.oauth.provider;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GithubUserInfo extends OAuth2UserInfo {

    @JsonProperty("id")
    private String id;
    @JsonProperty("login")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("avatar_url")
    private String imageUrl;

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GITHUB;
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean isGithub() {
        return true;
    }
}
