package com.devrace.global.config.oauth.provider;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); // 프로바이더 식별자

    public abstract String getName(); // 닉네임

    public abstract String getEmail(); // 이메일

    public abstract String getImageUrl(); // 프로파일 이미지

}
