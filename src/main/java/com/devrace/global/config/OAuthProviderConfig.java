package com.devrace.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.devrace.global.config.oauth.provider.ProviderType;

import jakarta.annotation.PostConstruct;

@Configuration
public class OAuthProviderConfig {

	@Value("${oauth.google.client-id}")
	private String googleClientId;

	@Value("${oauth.google.client-secret}")
	private String googleClientSecret;

	@Value("${oauth.github.client-id}")
	private String githubClientId;

	@Value("${oauth.github.client-secret}")
	private String githubClientSecret;

	@Value("${oauth.kakao.client-id}")
	private String kakaoClientId;

	@Value("${oauth.kakao.client-secret}")
	private String kakaoClientSecret;

	@PostConstruct
	public void init() {
		ProviderType.GOOGLE.init(googleClientId, googleClientSecret);
		ProviderType.GITHUB.init(githubClientId, githubClientSecret);
		ProviderType.KAKAO.init(kakaoClientId, kakaoClientSecret);
	}
}
