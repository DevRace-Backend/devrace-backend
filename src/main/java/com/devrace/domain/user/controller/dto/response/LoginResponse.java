package com.devrace.domain.user.controller.dto.response;

import com.devrace.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

	private String nickname;
	private String userImageUrl;
	private String levelImageUrl;

	public static LoginResponse from(User user) {
		return new LoginResponse(user.getNickname(), user.getImageUrl(), "empty");
	}
}
