package com.devrace.global.config.oauth.provider;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;
    private String registrationId;
    private ProviderType providerType;

    public OAuth2UserInfo(Map<String, Object> attributes, String registrationId, ProviderType providerType) {
        this.attributes = attributes;
        this.registrationId = registrationId;
        this.providerType = providerType;
    }

    public Map<String, Object> getAttributes() { return attributes; }

    public String getRegistrationId() {
        return registrationId;
    }

    public ProviderType getProviderType() { return providerType; }

    public abstract String getProviderId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public boolean isGithub() {
        return false;
    }

}
