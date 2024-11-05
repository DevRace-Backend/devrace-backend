package com.devrace.domain.follow.controller;

import com.devrace.domain.follow.controller.dto.FollowDto;
import com.devrace.domain.follow.controller.dto.FollowerResponseDto;
import com.devrace.domain.follow.controller.dto.FollowingResponseDto;
import com.devrace.domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<FollowingResponseDto> followUser(@RequestBody FollowDto followDto) {
        FollowingResponseDto responseDto = followService.followUser(followDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<Void> unfollowUser(@RequestBody FollowDto followDto) {
        followService.unfollowUser(followDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowerResponseDto>> getFollowerList(
            @PathVariable Long userId,
            @AuthenticationPrincipal Long myUserId
    ) {
        List<FollowerResponseDto> followerList = followService.getFollowerList(myUserId, userId);
        return ResponseEntity.ok(followerList);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<FollowingResponseDto>> getFollowingList(
            @PathVariable Long userId,
            @AuthenticationPrincipal Long myUserId
    ) {
        List<FollowingResponseDto> followingList = followService.getFollowingList(myUserId, userId);
        return ResponseEntity.ok(followingList);
    }
}
