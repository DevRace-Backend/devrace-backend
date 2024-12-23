package com.devrace.domain.follow.service;

import com.devrace.domain.category_visibility.entity.CategoryVisibility;
import com.devrace.domain.category_visibility.enums.CategoryType;
import com.devrace.domain.category_visibility.repository.CategoryVisibilityRepository;
import com.devrace.domain.follow.controller.dto.FollowDto;
import com.devrace.domain.follow.controller.dto.FollowerResponseDto;
import com.devrace.domain.follow.controller.dto.FollowingResponseDto;
import com.devrace.domain.follow.entity.Follower;
import com.devrace.domain.follow.entity.Following;
import com.devrace.domain.follow.repository.follower.FollowerRepository;
import com.devrace.domain.follow.repository.following.FollowingRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final FollowingRepository followingRepository;
    private final CategoryVisibilityRepository categoryVisibilityRepository;

    @Transactional
    public FollowingResponseDto followUser(FollowDto followDto, Long userId) {
        if (!userId.equals(followDto.getFollowerId())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User follower = getUser(userId);

        User following = getUser(followDto.getFollowingId());

        Follower followerEntity = Follower.builder()
                .follower(follower)
                .following(following)
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
        followerRepository.save(followerEntity);

        Following followingEntity = Following.builder()
                .follower(follower)
                .following(following)
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
        followingRepository.save(followingEntity);


        return FollowingResponseDto.builder()
                .id(followingEntity.getId())
                .followingNickname(following.getNickname())
                .imageUrl(following.getImageUrl())
                .description(following.getDescription())
                .isFollowing(checkFollowEachOther(follower, following))
                .build();
    }

    @Transactional
    public void unfollowUser(FollowDto followDto, Long userId) {
        if (!userId.equals(followDto.getFollowerId())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User follower = userRepository.findById(followDto.getFollowerId())
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOWER_NOT_FOUND));

        User following = userRepository.findById(followDto.getFollowingId())
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOWING_NOT_FOUND));

        Follower followerEntity =
                followerRepository.findByFollowerAndFollowing(follower, following)
                        .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));
        followerRepository.delete(followerEntity);

        Following followingEntity =
                followingRepository.findByFollowerAndFollowing(follower, following)
                        .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));
        followingRepository.delete(followingEntity);
    }

    public List<FollowerResponseDto> getFollowerList(Long myUserId, Long userId) {
        User myUser = getUser(myUserId);
        User user = getUser(userId);

        // 내가 나의 팔로워리스트 조회
        if (myUserId.equals(userId)) {
            return followerRepository.findByFollowing(user).stream()
                    .map(follower -> FollowerResponseDto.builder()
                            .id(follower.getId())
                            .followerNickname(follower.getFollower().getNickname())
                            .imageUrl(follower.getFollower().getImageUrl())
                            .isFollowing(checkFollowEachOther(follower.getFollower(), myUser))
                            .build()).
                    toList();
        }

        CategoryVisibility categoryVisibility = getCategoryVisibility(userId);

        boolean isPrivate = !categoryVisibility.isPublic();

        if (isPrivate) {
            throw new CustomException(ErrorCode.USER_FOLLOWER_LIST_PRIVATE);
        }

        return followerRepository.findByFollowing(user).stream()
                .map(follower -> FollowerResponseDto.builder()
                        .id(follower.getId())
                        .followerNickname(follower.getFollower().getNickname())
                        .imageUrl(follower.getFollower().getImageUrl())
                        .isFollowing(checkFollowEachOther(follower.getFollower(), myUser))
                        .build())
                .toList();
    }

    public List<FollowingResponseDto> getFollowingList(Long myUserId, Long userId) {
        User myUser = getUser(myUserId);
        User user = getUser(userId);

        if (myUserId.equals(userId)) {
            return followingRepository.findByFollower(user).stream()
                    .map(following -> FollowingResponseDto.builder()
                            .id(following.getId())
                            .followingNickname(following.getFollowing().getNickname())
                            .imageUrl(following.getFollowing().getImageUrl())
                            .description(following.getFollowing().getDescription())
                            .isFollowing(checkFollowEachOther(myUser, following.getFollowing()))
                            .build()
                    ).toList();
        }

        CategoryVisibility categoryVisibility = getCategoryVisibility(userId);

        boolean isPrivate = !categoryVisibility.isPublic();

        if (isPrivate) {
            throw new CustomException(ErrorCode.USER_FOLLOWING_LIST_PRIVATE);
        }

        return followingRepository.findByFollower(user).stream()
                .map(following -> FollowingResponseDto.builder()
                        .id(following.getId())
                        .followingNickname(following.getFollowing().getNickname())
                        .imageUrl(following.getFollowing().getImageUrl())
                        .description(following.getFollowing().getDescription())
                        .isFollowing(checkFollowEachOther(myUser, following.getFollowing()))
                        .build()
                ).toList();
    }

    public List<FollowerResponseDto> searchFollower(String nickname, Long myUserId, Long targetUserId) {
        User targetUser = getUser(targetUserId);
        User my = getUser(myUserId);

        List<Follower> followerList = followerRepository.searchFollower(nickname, targetUser.getId());

        return followerList.stream()
            .map(follower -> FollowerResponseDto.builder()
                .id(follower.getId())
                .followerNickname(follower.getFollower().getNickname())
                .imageUrl(follower.getFollower().getImageUrl())
                .description(follower.getFollower().getDescription())
                .isFollowing(checkFollowEachOther(my, follower.getFollower()))
                .build())
            .toList();
    }

    public List<FollowingResponseDto> searchFollowing(String nickname, Long myUserId, Long targetUserId) {
        User targetUser = getUser(targetUserId);
        User my = getUser(myUserId);

        List<Following> followingList = followingRepository.searchFollowing(nickname, targetUser.getId());

        return followingList.stream()
            .map(following -> FollowingResponseDto.builder()
                .id(following.getId())
                .followingNickname(following.getFollowing().getNickname())
                .imageUrl(following.getFollowing().getImageUrl())
                .description(following.getFollowing().getDescription())
                .isFollowing(checkFollowEachOther(my, following.getFollowing()))
                .build())
            .toList();
    }


    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     *  특정 사용자가 나를 팔로우하고 있는지 확인하는 로직
     *
     *  B(following)가  A(follower)를 팔로우하는지 확인하는 로직
     */
    private boolean checkFollowEachOther(User follower, User following) {
        return followerRepository.findByFollowerAndFollowing(following, follower).isPresent();
    }

    private CategoryVisibility getCategoryVisibility(Long userId) {
        CategoryVisibility categoryVisibility = categoryVisibilityRepository.findByUserIdAndType(userId, CategoryType.FOLLOW)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_VISIBILITY_NOT_FOUND));
        return categoryVisibility;
    }
}
