package com.devrace.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devrace.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByNickname(닉네임): 닉네임을 받아 사용자를 조회한다.")
    void findByNickname() {
        // given
        final String nickname = "Hut234";
        final String email = "test@gmail.com";

        User user = createUser(nickname, email);
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByNickname(nickname);

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(user);
        assertThat(result.get().getNickname()).isEqualTo(user.getNickname());
        assertThat(result.get().getPrimaryEmail()).isEqualTo(user.getPrimaryEmail());
    }

    private User createUser(String nickname, String email) {
        return new User(nickname, email, null, "tester");
    }
}