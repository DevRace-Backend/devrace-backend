package com.devrace.global.config.oauth.provider;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return String.valueOf(properties.get("nickname"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return String.valueOf(kakaoAccount.get("email"));
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
//        properties.get("profile_image"); // 640 x 640
//        properties.get("thumbnail_image"); // 110 x 110
        return String.valueOf(attributes.get("thumbnail_image"));
    }
}
