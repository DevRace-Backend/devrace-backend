package com.devrace.global.config.oauth.provider;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes, String registrationId, ProviderType providerType) {
        super(attributes, registrationId, providerType);
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("login"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getImageUrl() {
        return String.valueOf(attributes.get("avatar_url"));
    }

    @Override
    public boolean isGithub() {
        return true;
    }
}
