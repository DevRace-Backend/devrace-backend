package com.devrace.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.service.UserService;
import com.devrace.global.config.jwt.JwtTokenProvider;
import com.devrace.global.config.oauth.provider.GithubUserInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TokenController {


	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;

	@GetMapping("/tokens")
	public String getToken(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String email,
		@RequestParam String imageUrl) {
		User user = userService.getUser(new GithubUserInfo(id, name, email, imageUrl));
		return jwtTokenProvider.createAccessToken(user.getId(), "ROLE_USER");
	}
}
