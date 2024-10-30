package com.devrace.domain.user.service;

import static com.devrace.global.exception.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devrace.domain.user.controller.dto.request.BlogAddressUpdateRequest;
import com.devrace.domain.user.controller.dto.request.DescriptionUpdateRequest;
import com.devrace.domain.user.controller.dto.request.NicknameUpdateRequest;
import com.devrace.domain.user.controller.dto.response.MyInfoResponse;
import com.devrace.domain.user.controller.dto.response.UserInfoResponse;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getMyInfo(유저PK): 유저 PK를 받아 사용자를 조회한다.")
    void getMyInfo_success() {
        // given
        User user = createUser("Hut234");
        User savedUser = userRepository.save(user);

        final Long userId = savedUser.getId();

        // when
        MyInfoResponse myInfo = userService.getMyInfo(userId);

        // then
        assertThat(myInfo.getImageUrl()).isEqualTo(user.getImageUrl());
        assertThat(myInfo.getNickname()).isEqualTo(user.getNickname());
        assertThat(myInfo.getDescription()).isEqualTo(user.getDescription());
        assertThat(myInfo.getBlogAddress()).isEqualTo(user.getBlogAddress());
        assertThat(myInfo.getGithubName()).isEqualTo(user.getGithubName());
    }

    @Test
    @DisplayName("getMyInfo(유저PK): 유저 PK를 받아 사용자를 조회한다.")
    void getMyInfo_does_not_exist_id_fail() {
        // given
        User user1 = createUser("Hut234");
        User user2 = createUser("Tester1");
        User user3 = createUser("Tester2");
        User user4 = createUser("Tester3");
        userRepository.saveAll(List.of(user1, user2, user3, user4));

        final Long wrongUserId = Long.MAX_VALUE;

        // expected
        assertThatThrownBy(() -> userService.getMyInfo(wrongUserId))
                .isInstanceOf(CustomException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("getUserInfo(닉네임): 닉네임을 받아 사용자를 조회한다.")
    void getUserInfo_success() {
        // given
        final String nickname1 = "Hut234";
        final String nickname2 = "doraemi";

        User user1 = createUser(nickname1);
        User user2 = createUser(nickname2);
        userRepository.saveAll(List.of(user1, user2));

        // when
        UserInfoResponse userInfo = userService.getUserInfo(nickname1);

        // then
        assertThat(userInfo.getNickname()).isEqualTo(user1.getNickname());
    }

    @Test
    @DisplayName("getUserInfo(닉네임): 닉네임 대소문자가 다른 경우 예외가 발생한다.")
    void getUserInfo_letter_case_fail() {
        // given
        final String nickname = "Hut234";
        final String different_letter_case = "hut234";

        User user = createUser(nickname);
        userRepository.save(user);

        // expected
        assertThatThrownBy(() -> userService.getUserInfo(different_letter_case))
                .isInstanceOf(CustomException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("updateNickname(유저PK, 변경할 닉네임): 변경할 닉네임을 입력받아 사용자 닉네임을 변경한다.")
    void updateNickname_success() {
        // given
        User user = createUser("Hut234");
        User savedUser = userRepository.save(user);

        final Long userId = savedUser.getId();
        final String newNickname = "NewNickname";
        final NicknameUpdateRequest request = new NicknameUpdateRequest(newNickname);

        // when
        userService.updateNickname(userId, request);

        // then
        assertThat(request.getNickname()).isEqualTo(savedUser.getNickname());
    }

    @Test
    @DisplayName("updateDescription(유저PK, 변경할 자기소개): 변경할 자기소개를 입력받아 사용자 자기소개를 변경한다.")
    void updateDescription_success() {
        // given
        User user = createUser("Hut234");
        User savedUser = userRepository.save(user);

        final Long userId = savedUser.getId();
        final String newDescription = "NewDescription";
        final DescriptionUpdateRequest request = new DescriptionUpdateRequest(newDescription);

        // when
        userService.updateDescription(userId, request);

        // then
        assertThat(request.getDescription()).isEqualTo(savedUser.getDescription());
    }

    @Test
    @DisplayName("updateBlogAddress(유저PK, 변경할 블로그 주소): 변경할 블로그 주소를 입력받아 사용자 블로그 주소를 변경한다.")
    void updateBlogAddress_success() {
        // given
        User user = createUser("Hut234");
        User savedUser = userRepository.save(user);

        final Long userId = savedUser.getId();
        final String newBlogAddress = "https://NewBlogAddress";
        final BlogAddressUpdateRequest request = new BlogAddressUpdateRequest(newBlogAddress);

        // when
        userService.updateBlogAddress(userId, request);

        // then
        assertThat(request.getBlogAddress()).isEqualTo(savedUser.getBlogAddress());
    }


    private User createUser(String nickname) {
        return new User(nickname, "test@gmail.com", "http://imageUrl.com", true, "tester");
    }
}