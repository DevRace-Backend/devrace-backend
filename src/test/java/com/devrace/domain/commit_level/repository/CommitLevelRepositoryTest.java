package com.devrace.domain.commit_level.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devrace.domain.commit_level.entity.CommitLevel;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
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
class CommitLevelRepositoryTest {

    @Autowired
    private CommitLevelRepository commitLevelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUserId(유저 PK): 유저 PK를 통해서 CommitLevel을 조회한다.")
    void findByUserId() {
        // given
        final User tester = createUser("tester", "tester@gmail.com", "tester");
        final CommitLevel commitLevel = new CommitLevel("잔디", "http://test", tester);

        commitLevelRepository.save(commitLevel);

        // when
        Optional<CommitLevel> result = commitLevelRepository.findByUserId(tester.getId());

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(commitLevel);
    }

    private User createUser(String nickname, String email, String githubName) {
        return userRepository.save(new User(nickname, email, "http://imageUrl.com", true, githubName));
    }
}