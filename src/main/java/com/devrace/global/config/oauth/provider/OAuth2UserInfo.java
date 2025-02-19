package com.devrace.global.config.oauth.provider;

public abstract class OAuth2UserInfo {

	public abstract ProviderType getProviderType();

	public abstract String getProviderId();

	public abstract String getName();

	public abstract String getEmail();

	public abstract String getImageUrl();

	public boolean isGithub() {
		return false;
	}
}
