package com.devrace.global.config.oauth.provider;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleUserInfo extends OAuth2UserInfo {

    @JsonProperty("sub")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("picture")
    private String imageUrl;

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GOOGLE;
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
}
