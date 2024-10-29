package com.devrace.domain.user.service;

import static com.devrace.global.exception.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devrace.domain.user.controller.dto.UserInfoResponse;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
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
        assertThat(userInfo.getNickname()).isEqualTo(nickname1);
    }

    @Test
    @DisplayName("getUserInfo(닉네임): 닉네임 대소문자가 다른 경우 예외가 발생한다.")
    void getUserInfo_letter_case_fail() {
        // given
        final String nickname = "Hut234";

        User user = createUser(nickname);

        userRepository.save(user);

        final String different_letter_case = "hut234";

        // expected
        assertThatThrownBy(() -> userService.getUserInfo(different_letter_case))
                .isInstanceOf(CustomException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    private User createUser(String nickname) {
        return new User(nickname, "test@gmail.com", null, "tester");
    }
}