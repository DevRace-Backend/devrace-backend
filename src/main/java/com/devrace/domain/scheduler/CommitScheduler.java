package com.devrace.domain.scheduler;

import com.devrace.domain.commit.service.CommitService;
import com.devrace.domain.user.repository.UserRepository;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommitScheduler {

    private final CommitService commitService;
    private final UserRepository userRepository;

    /**
     * 6시간마다 모든 유저 커밋기록 검사
     */
    @Scheduled(cron = "0 0 */6 * * *")
    public void updateCommitScheduler() {
        ZonedDateTime toDate = ZonedDateTime.now(ZoneOffset.UTC);
        userRepository.findAll().forEach(user -> {
            try {
                commitService.collectCommitData(user.getId(), toDate);
            } catch (Exception e) {
                log.error("문제 발생.  User :", user.getId(), ",  message : ", e.getMessage());
            }
        });
    }

}
