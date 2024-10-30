package com.devrace.domain.user.controller;

import com.devrace.domain.user.controller.dto.request.BlogAddressUpdateRequest;
import com.devrace.domain.user.controller.dto.request.DescriptionUpdateRequest;
import com.devrace.domain.user.controller.dto.request.NicknameUpdateRequest;
import com.devrace.domain.user.controller.dto.response.MyInfoResponse;
import com.devrace.domain.user.controller.dto.response.UserInfoResponse;
import com.devrace.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> updateNickname(@AuthenticationPrincipal Long userId, @Valid @RequestBody NicknameUpdateRequest request) {
        userService.updateNickname(userId, request);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/me/description")
    public ResponseEntity<Void> updateDescription(@AuthenticationPrincipal Long userId, @Valid @RequestBody DescriptionUpdateRequest request) {
        userService.updateDescription(userId, request);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/me/blog-address")
    public ResponseEntity<Void> updateBlogAddress(@AuthenticationPrincipal Long userId, @RequestBody BlogAddressUpdateRequest request) {
        userService.updateBlogAddress(userId, request);
        return ResponseEntity.ok(null);
    }
}
