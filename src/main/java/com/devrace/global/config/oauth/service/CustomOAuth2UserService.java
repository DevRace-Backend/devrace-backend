package com.devrace.global.config.oauth.service;

import com.devrace.global.config.oauth.provider.OAuth2Provider;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        OAuth2UserInfo oAuth2UserInfo = OAuth2Provider.getUserInfo(clientRegistration.getRegistrationId(), oAuth2User.getAttributes());
        return null;
    }
}
