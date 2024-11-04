package com.devrace.domain.scheduler;

import com.devrace.domain.commit.entity.CommitHistory;
import com.devrace.domain.commit.repository.CommitHistoryRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ActiveProfiles("test")
class CommitHistorySchedulerTest {

    @Autowired
    private CommitHistoryScheduler commitHistoryScheduler;

    @Autowired
    private CommitHistoryRepository commitHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = createUser("testNickname", "test@test.com", "testGithubName");
        userRepository.save(user);

        CommitHistory commitHistory = CommitHistory.builder()
                .user(user)
                .year(2024)
                .month(11)
                .week(45)
                .day(1)
                .commitCount(10)
                .build();
        commitHistoryRepository.save(commitHistory);
    }

    @Test
    @Transactional
    @DisplayName("월간 커밋 저장")
    void testSaveMonthlyCommitHistory() {
        commitHistoryScheduler.saveMonthlyCommitHistory();
        assertThat(commitHistoryRepository.findAll()).isNotEmpty();
    }

    @Test
    @Transactional
    @DisplayName("주간 커밋 저장")
    void testSaveWeeklyCommitHistory() {
        commitHistoryScheduler.saveWeeklyCommitHistory();
        assertThat(commitHistoryRepository.findAll()).isNotEmpty();
    }

    @Test
    @Transactional
    @DisplayName("일간 커밋 저장")
    void saveDailyCommitHistory() {
        commitHistoryScheduler.saveDailyCommitHistory();
        assertThat(commitHistoryRepository.findAll()).isNotEmpty();
    }

    private User createUser(String nickname, String email, String githubName) {
        return new User(nickname, email, "http://imageUrl.com", true, githubName);
    }
}