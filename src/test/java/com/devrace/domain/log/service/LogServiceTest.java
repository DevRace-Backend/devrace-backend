package com.devrace.domain.log.service;

import com.devrace.config.TestAsyncConfig;
import com.devrace.domain.log.controller.LogController;
import com.devrace.domain.log.controller.dto.SubmitLogDto;
import com.devrace.domain.log.controller.dto.SubmitLogResponseDto;
import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.repository.LogRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LogServiceTest {

    @Autowired
    private LogController logController;

    @Autowired
    private LogService logService;

    @Autowired
    private LogRepository logRepository;

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
        logRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test1", roles = {"USER"})
    public void submitLog() {
        SubmitLogDto submitLogDto = SubmitLogDto.builder()
                .address("https://asdasd.com")
                .title("title")
                .content("content")
                .build();

        ResponseEntity<SubmitLogResponseDto> response = logController.submitLog(user.getId(), submitLogDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SubmitLogResponseDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(submitLogDto.getAddress(), responseDto.getAddress());

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Log saveLog = logRepository.findByAddressAndUserId(submitLogDto.getAddress(), user.getId())
                            .orElseThrow(() -> new AssertionError("로그 못찾음"));
                    assertEquals(submitLogDto.getAddress(), saveLog.getAddress());
                    assertEquals(submitLogDto.getTitle(), saveLog.getTitle());
                    assertEquals(submitLogDto.getContent(), saveLog.getContent());
                });



//        SubmitLogResponseDto responseDto = SubmitLogResponseDto.builder()
//                .status("성공")
//                .message("성공")
//                .logId(1L)
//                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
//                .address("https://asdasd.com")
//                .build();
//
//        Mockito.when(logService.submitLog(any(SubmitLogDto.class), eq(1L))).thenReturn(responseDto);
//
//        ResponseEntity<SubmitLogResponseDto> response = logController.submitLog(1L, submitLogDto);
//
//        assertNotNull(response);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("성공", response.getBody().getStatus());
//        verify(logService, times(1)).submitLog(any(SubmitLogDto.class), eq(1L));

    }

    private User createUser(String testNickname, String mail, String testGithubName) {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return new User(testNickname + uniqueSuffix, mail, "http://imageUrl.com", true, testGithubName + uniqueSuffix);
    }
}