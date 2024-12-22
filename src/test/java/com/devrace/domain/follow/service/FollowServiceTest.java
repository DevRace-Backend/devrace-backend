package com.devrace.domain.follow.service;

import com.devrace.domain.category_visibility.entity.CategoryVisibility;
import com.devrace.domain.category_visibility.enums.CategoryType;
import com.devrace.domain.category_visibility.repository.CategoryVisibilityRepository;
import com.devrace.domain.follow.controller.dto.FollowDto;
import com.devrace.domain.follow.repository.follower.FollowerRepository;
import com.devrace.domain.follow.repository.following.FollowingRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private FollowingRepository followingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryVisibilityRepository categoryVisibilityRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = createUser("testNickname1", "test" + UUID.randomUUID().toString().substring(0, 4) + "@test.com", "testGithubName1");
        user2 = createUser("testNickname2", "test" + UUID.randomUUID().toString().substring(0, 4) + "@test.com", "testGithubName2");
        user3 = createUser("testNickname2", "test" + UUID.randomUUID().toString().substring(0, 4) + "@test.com", "testGithubName2");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        categoryVisibilityRepository.save(new CategoryVisibility(CategoryType.FOLLOW, user1));
        categoryVisibilityRepository.save(new CategoryVisibility(CategoryType.FOLLOW, user2));
        categoryVisibilityRepository.save(new CategoryVisibility(CategoryType.FOLLOW, user3));
    }

    @AfterEach
    void cleanUp() {
        followingRepository.deleteAllInBatch();
        followerRepository.deleteAllInBatch();
        categoryVisibilityRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    void followUser() {
        FollowDto followDto = new FollowDto(user1.getId(), user2.getId());

        followService.followUser(followDto, user1.getId());

        assertThat(followerRepository.findByFollowerAndFollowing(user1, user2)).isPresent();
        assertThat(followingRepository.findByFollowerAndFollowing(user1, user2)).isPresent();
    }

    @Test
    void unfollowUser() {
        FollowDto followDto = new FollowDto(user1.getId(), user2.getId());

        followService.followUser(followDto, user1.getId());
        followService.unfollowUser(followDto, user1.getId());

        assertThat(followerRepository.findByFollowerAndFollowing(user1, user2)).isNotPresent();
        assertThat(followingRepository.findByFollowerAndFollowing(user1, user2)).isNotPresent();
    }

    @Test
    @DisplayName("내가 내 팔로워리스트 봄")
    void getFollowerList() {
        FollowDto followDto = new FollowDto(user2.getId(), user1.getId());

        followService.followUser(followDto, user2.getId());

        List<?> followerList = followService.getFollowerList(user1.getId(), user1.getId());

        assertThat(followerList).hasSize(1);
        assertThat(followerList.get(0)).hasFieldOrPropertyWithValue("followerNickname", user2.getNickname());
    }

    @Test
    @DisplayName("내가 다른 사람 팔로워리스트 봄")
    void getFollowerListOtherPeople() {
        FollowDto followDto = new FollowDto(user2.getId(), user3.getId());
        followService.followUser(followDto, user2.getId());

        List<?> followerList = followService.getFollowerList(user1.getId(), user3.getId());

        assertThat(followerList).hasSize(1);
        assertThat(followerList.get(0)).hasFieldOrPropertyWithValue("followerNickname", user2.getNickname());
    }

    @Test
    @DisplayName("내가 내 팔로잉리스트 봄")
    void getFollowingList() {
        FollowDto followDto = new FollowDto(user1.getId(), user2.getId());
        followService.followUser(followDto, user1.getId());

        List<?> followingList = followService.getFollowingList(user1.getId(), user1.getId());

        assertThat(followingList).hasSize(1);
        assertThat(followingList.get(0)).hasFieldOrPropertyWithValue("followingNickname", user2.getNickname());
    }

    @Test
    @DisplayName("내가 다른 사람 팔로잉리스트 봄")
    void getFollowingListOtherPeople() {
        FollowDto followDto = new FollowDto(user3.getId(), user2.getId());
        followService.followUser(followDto, user3.getId());

        List<?> followingList = followService.getFollowingList(user1.getId(), user3.getId());

        assertThat(followingList).hasSize(1);
        assertThat(followingList.get(0)).hasFieldOrPropertyWithValue("followingNickname", user2.getNickname());
    }
    private User createUser(String testNickname, String mail, String testGithubName) {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return new User(testNickname + uniqueSuffix, mail, "http://imageUrl.com", true, testGithubName + uniqueSuffix);
    }
}