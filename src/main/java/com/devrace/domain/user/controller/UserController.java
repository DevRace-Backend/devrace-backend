package com.devrace.domain.user.controller;

import com.devrace.domain.user.controller.dto.MyInfoResponse;
import com.devrace.domain.user.controller.dto.UserInfoResponse;
import com.devrace.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<MyInfoResponse> getUserById(@AuthenticationPrincipal Long userId) {
        MyInfoResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserByNickname(@RequestParam String nickname) {
        UserInfoResponse response = userService.getUserInfo(nickname);
        return ResponseEntity.ok(response);
    }
}
