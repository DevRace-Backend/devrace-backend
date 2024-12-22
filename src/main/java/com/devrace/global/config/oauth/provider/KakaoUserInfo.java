package com.devrace.global.config.oauth.provider;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoUserInfo extends OAuth2UserInfo {

    @JsonProperty("id")
    private String id;
    @JsonProperty("properties")
    private Properties properties;
    @JsonProperty("kakao_account")
    private KakaoAccount kakao_account;

    @Getter
    public static class Properties {
        @JsonProperty("nickname")
        private String name;
    }

    @Getter
    public static class KakaoAccount {
        @JsonProperty("email")
        private String email;
        @JsonProperty("profile")
        private Profile profile;

        @Getter
        public static class Profile {
            @JsonProperty("thumbnail_image_url")
            private String imageUrl;
        }
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getName() {
        return properties.getName();
    }

    @Override
    public String getEmail() {
        return kakao_account.getEmail();
    }

    @Override
    public String getImageUrl() {
        return kakao_account.getProfile().getImageUrl();
    }
}
