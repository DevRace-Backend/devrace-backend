package com.devrace.global.util;

import static com.devrace.global.exception.ErrorCode.*;
import static org.springframework.http.HttpMethod.*;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.config.oauth.provider.ProviderType;
import com.devrace.global.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SocialLoginService {

	private final RestTemplate restTemplate = new RestTemplate();

	public JsonNode getOAuthResponse(ProviderType provider, String code) {
		String uri = provider.getTokenUri();
		HttpHeaders headers = new HttpHeaders();
		headers.add(provider.getTokenHeaderName(), provider.getTokenHeaderValue());

		MultiValueMap<String, String> body = provider.buildRequestBody(code);
		RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
			.post(URI.create(uri))
			.headers(headers)
			.body(body);

		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

		try {
			return new ObjectMapper().readTree(response.getBody());
		} catch (JsonProcessingException e) {
			throw new CustomException(COMMON_BAD_REQUEST);
		}
	}

	public String extractToken(JsonNode jsonNode) {
		String tokenType = jsonNode.get("token_type").asText();
		String authorizationToken = jsonNode.get("access_token").asText();
		return tokenType.concat(" ").concat(authorizationToken);
	}

	public OAuth2UserInfo getUserInfo(ProviderType provider, String token) {
		String uri = provider.getUserInfoUri();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, token);
		headers.add(HttpHeaders.CONTENT_TYPE, provider.getUserInfoHeaderValue());

		return restTemplate.exchange(uri, GET, new HttpEntity<>(headers), provider.getUserInfo()).getBody();
	}
}
