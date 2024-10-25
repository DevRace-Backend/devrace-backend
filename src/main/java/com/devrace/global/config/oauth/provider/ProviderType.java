package com.devrace.global.config.oauth.provider;

import static com.devrace.global.exception.ErrorCode.INVALID_PROVIDER_ERROR;

import com.devrace.global.exception.CustomException;
import java.util.Arrays;
import java.util.Map;

public enum ProviderType {
    GOOGLE("google") {
        @Override
        public OAuth2UserInfo createUserInfo(Map<String, Object> attributes, String registrationId) {
            return new GoogleOAuth2UserInfo(attributes, registrationId, this);
        }
    },

    GITHUB("github") {
        @Override
        public OAuth2UserInfo createUserInfo(Map<String, Object> attributes, String registrationId) {
            return new GithubOAuth2UserInfo(attributes, registrationId, this);
        }
    },

    KAKAO("kakao") {
        @Override
        public OAuth2UserInfo createUserInfo(Map<String, Object> attributes, String registrationId) {
            return new KakaoOAuth2UserInfo(attributes, registrationId, this);
        }
    };

    public static ProviderType getProviderType(String registrationId) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equalsIgnoreCase(provider.registrationId))
                .findAny()
                .orElseThrow(() -> new CustomException(INVALID_PROVIDER_ERROR));
    }

    public static OAuth2UserInfo getUserInfo(String registrationId, Map<String, Object> attributes) {
        return getProviderType(registrationId)
                .createUserInfo(attributes, registrationId);
    }

    private final String registrationId;

    ProviderType(String registrationId) {
        this.registrationId = registrationId;
    }

    public abstract OAuth2UserInfo createUserInfo(Map<String, Object> attributes, String registrationId);
}
