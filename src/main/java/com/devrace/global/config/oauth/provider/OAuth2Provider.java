package com.devrace.global.config.oauth.provider;

import static com.devrace.global.exception.ErrorCode.INVALID_PROVIDER_ERROR;

import com.devrace.global.exception.CustomException;
import java.util.Arrays;
import java.util.Map;

public enum OAuth2Provider {
    GOOGLE("google") {
        @Override
        public OAuth2UserInfo createUserInfo(Map<String, Object> attributes) {
            return new GoogleOAuth2UserInfo(attributes);
        }
    },

    GITHUB("github") {
        @Override
        public OAuth2UserInfo createUserInfo(Map<String, Object> attributes) {
            return new GithubOAuth2UserInfo(attributes);
        }
    },

    KAKAO("kakao") {
        @Override
        public OAuth2UserInfo createUserInfo(Map<String, Object> attributes) {
            return new KakaoOAuth2UserInfo(attributes);
        }
    };

    public static OAuth2UserInfo getUserInfo(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equalsIgnoreCase(provider.registrationId))
                .findAny()
                .orElseThrow(() -> new CustomException(INVALID_PROVIDER_ERROR))
                .createUserInfo(attributes);
    }

    private final String registrationId;

    OAuth2Provider(String registrationId) {
        this.registrationId = registrationId;
    }

    public abstract OAuth2UserInfo createUserInfo(Map<String, Object> attributes);
}
