package com.devrace.global.config.oauth.provider;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.Getter;

@Getter
public enum ProviderType {
	GOOGLE(
		"google",
		"https://www.googleapis.com/oauth2/v4/token",
		"https://www.googleapis.com/oauth2/v3/userinfo",
		CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE, APPLICATION_JSON_VALUE,
		GoogleUserInfo.class),

	KAKAO(
		"kakao",
		"https://kauth.kakao.com/oauth/token",
		"https://kapi.kakao.com/v2/user/me",
		CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE, APPLICATION_FORM_URLENCODED_VALUE,
		KakaoUserInfo.class),

	GITHUB(
		"github",
		"https://github.com/login/oauth/access_token",
		"https://api.github.com/user",
		ACCEPT, APPLICATION_JSON_VALUE, APPLICATION_JSON_VALUE,
		GithubUserInfo.class);;

	private final String registrationId;
	private final String tokenUri;
	private final String userInfoUri;
	private final String tokenHeaderName;
	private final String tokenHeaderValue;
	private final String userInfoHeaderValue;
	private final Class<? extends OAuth2UserInfo> userInfo;
	private String clientId;
	private String clientSecret;

	ProviderType(
		String registrationId,
		String tokenUri, String userInfoUri,
		String tokenHeaderName, String tokenHeaderValue, String userInfoHeaderValue,
		Class<? extends OAuth2UserInfo> userInfo) {
		this.registrationId = registrationId;
		this.tokenUri = tokenUri;
		this.userInfoUri = userInfoUri;
		this.tokenHeaderName = tokenHeaderName;
		this.tokenHeaderValue = tokenHeaderValue;
		this.userInfoHeaderValue = userInfoHeaderValue;
		this.userInfo = userInfo;
	}

	public void init(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public MultiValueMap<String, String> buildRequestBody(String code) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("code", code);
		body.add("grant_type", "authorization_code");
		body.add("redirect_uri", "http://localhost:3000/auth/callback");
		return body;
	}
}
