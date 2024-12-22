package com.devrace.domain.follow.controller;

import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmResponseDto;
import com.devrace.domain.follow.controller.dto.FollowDto;
import com.devrace.domain.follow.controller.dto.FollowerResponseDto;
import com.devrace.domain.follow.controller.dto.FollowingResponseDto;
import com.devrace.domain.follow.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "유저 팔로우", description = "유저 팔로우 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "해당 유저를 팔로우하였습니다.",
                    content = @Content(schema = @Schema(implementation = FollowingResponseDto.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/follow")
    public ResponseEntity<FollowingResponseDto> followUser(
        @RequestBody FollowDto followDto,
        @AuthenticationPrincipal Long userId) {
        FollowingResponseDto responseDto = followService.followUser(followDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "유저 언팔로우", description = "유저 언팔로우 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "해당 유저를 언팔로우하였습니다.",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "팔로우 관계를 찾을 수 없습니다. 또는 나를 팔로우 하는 유저를 찾을 수 없습니다. 또는 내가 팔로잉하는 유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/unfollow")
    public ResponseEntity<Void> unfollowUser(
        @RequestBody FollowDto followDto,
        @AuthenticationPrincipal Long userId) {
        followService.unfollowUser(followDto, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "팔로워리스트 조회", description = "팔로워 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "팔로워 리스트를 조회하였습니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FollowerResponseDto.class)))),
            @ApiResponse(
                    responseCode = "403",
                    description = "해당 유저의 팔로워 리스트는 비공개입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowerResponseDto>> getFollowerList(
            @PathVariable Long userId,
            @AuthenticationPrincipal Long myUserId
    ) {
        List<FollowerResponseDto> followerList = followService.getFollowerList(myUserId, userId);
        return ResponseEntity.ok(followerList);
    }

    @Operation(summary = "팔로잉리스트 조회", description = "팔로잉 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "팔로잉 리스트를 조회하였습니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FollowingResponseDto.class)))),
            @ApiResponse(
                    responseCode = "403",
                    description = "해당 유저의 팔로잉 리스트는 비공개입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/following/{userId}")
    public ResponseEntity<List<FollowingResponseDto>> getFollowingList(
            @PathVariable Long userId,
            @AuthenticationPrincipal Long myUserId
    ) {
        List<FollowingResponseDto> followingList = followService.getFollowingList(myUserId, userId);
        return ResponseEntity.ok(followingList);
    }

    @GetMapping("/search/follower")
    public ResponseEntity<List<FollowerResponseDto>> searchFollower(
        @RequestParam(required = false) String nickname,
        @RequestParam Long myUserId,
        @RequestParam Long targetUserId
    ) {
        List<FollowerResponseDto> responseDtoList = followService.searchFollower(nickname, myUserId, targetUserId);
        return ResponseEntity.ok(responseDtoList);
    }

}
