package com.devrace.domain.algorithm.solution.service;

import com.devrace.domain.algorithm.problem.entity.Problem;
import com.devrace.domain.algorithm.problem.repository.ProblemRepository;
import com.devrace.domain.algorithm.solution.controller.AlgorithmController;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.algorithm.solution.repository.AlgorithmCountRepository;
import com.devrace.domain.algorithm.solution.repository.AlgorithmRepository;
import com.devrace.domain.log.controller.LogController;
import com.devrace.domain.log.repository.LogRepository;
import com.devrace.domain.log.service.LogService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import org.awaitility.Awaitility;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AlgorithmServiceTest {

    @Autowired
    private AlgorithmController algorithmController;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private AlgorithmCountRepository algorithmCountRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = createUser("testNickname1", "test" + UUID.randomUUID().toString().substring(0, 4) + "@test.com", "testGithubName1");
        userRepository.save(user);
    }

    @AfterEach
    void clear() {
        algorithmCountRepository.deleteById(user.getId());
        algorithmRepository.deleteById(user.getId());
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test1", roles = {"USER"})
    void submitAlgorithm() {
        SubmitAlgorithmDto submitAlgorithmDto = SubmitAlgorithmDto.builder()
                .userId(user.getId())
                .address("https://school.programmers.co.kr")
                .title("title")
                .description("desc")
                .review("리뷰")
                .isPublic(true)
                .build();

        ResponseEntity<SubmitAlgorithmResponseDto> response = algorithmController.submitAlgorithm(user.getId(), submitAlgorithmDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SubmitAlgorithmResponseDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(submitAlgorithmDto.getAddress(), responseDto.getAddress());
        assertEquals(submitAlgorithmDto.getReview(), responseDto.getReview());
        assertEquals(submitAlgorithmDto.getDescription(), responseDto.getDescription());

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Solution saveSolution = algorithmRepository.findByIdAndUserId(responseDto.getId(), user.getId())
                            .orElseThrow(() -> new AssertionError("알고리즘 제출내역 못찾음"));
                    assertEquals(submitAlgorithmDto.getDescription(), saveSolution.getDescription());
                    assertEquals(submitAlgorithmDto.getReview(), saveSolution.getReview());

                    Problem saveProblem = problemRepository.findByAddressAndTitle(submitAlgorithmDto.getAddress(), submitAlgorithmDto.getTitle())
                            .orElseThrow(() -> new AssertionError("알고리즘 문제 못찾음"));
                    assertEquals(submitAlgorithmDto.getAddress(), saveProblem.getAddress());
                    assertEquals(submitAlgorithmDto.getTitle(), saveProblem.getTitle());
                });
    }

    private User createUser(String testNickname, String mail, String testGithubName) {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return new User(testNickname + uniqueSuffix, mail, "http://imageUrl.com", true, testGithubName + uniqueSuffix);
    }
}