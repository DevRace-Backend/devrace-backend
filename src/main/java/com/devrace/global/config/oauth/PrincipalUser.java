package com.devrace.global.config.oauth;

import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalUser implements OAuth2User {

    private final OAuth2UserInfo userInfo;

    public PrincipalUser(OAuth2UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public OAuth2UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return userInfo.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return userInfo.getName();
    }
}
