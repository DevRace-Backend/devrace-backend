package com.devrace.domain.scheduler;

import com.devrace.domain.algorithm.solution.entity.AlgorithmHistory;
import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.algorithm.solution.repository.AlgorithmHistoryRepository;
import com.devrace.domain.algorithm.solution.repository.AlgorithmRepository;
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
class AlgorithmHistorySchedulerTest {

    @Autowired
    private AlgorithmHistoryScheduler algorithmHistoryScheduler;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private AlgorithmHistoryRepository algorithmHistoryRepository;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {
        User user = createUser("testNickname", "test@test.com", "testGithubName");
        userRepository.save(user);

        Solution solution = createSolution(user,"content");

        AlgorithmHistory algorithmHistory = AlgorithmHistory.builder()
                .year(2024)
                .month(11)
                .week(45)
                .day(1)
                .solutionCount(10)
                .solution(solution)
                .build();
        algorithmHistoryRepository.save(algorithmHistory);
    }

    @Test
    @Transactional
    @DisplayName("월간 알고리즘 저장")
    void saveMonthlyAlgorithmHistory() {
        algorithmHistoryScheduler.saveMonthlyAlgorithmHistory();
        assertThat(algorithmHistoryRepository.findAll()).isNotEmpty();
    }

    @Test
    @Transactional
    @DisplayName("주간 알고리즘 저장")
    void saveWeeklyAlgorithmHistory() {
        algorithmHistoryScheduler.saveWeeklyAlgorithmHistory();
        assertThat(algorithmHistoryRepository.findAll()).isNotEmpty();
    }

    @Test
    @Transactional
    @DisplayName("일간 알고리즘 저장")
    void saveDailyAlgorithmHistory() {
        algorithmHistoryScheduler.saveDailyAlgorithmHistory();
        assertThat(algorithmHistoryRepository.findAll()).isNotEmpty();
    }

    private User createUser(String nickname, String email, String githubName) {
        return new User(nickname, email, "http://imageUrl.com", true, githubName);
    }

    private Solution createSolution(User user, String content) {
        return Solution.builder()
                .user(user)
                .description(content)
                .build();
    }
}