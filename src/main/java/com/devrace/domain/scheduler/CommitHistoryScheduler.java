package com.devrace.domain.scheduler;

import com.devrace.domain.commit.entity.CommitHistory;
import com.devrace.domain.commit.repository.CommitHistoryRepository;
import com.devrace.domain.commit.service.CommitService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommitHistoryScheduler {

    private final CommitHistoryRepository commitHistoryRepository;
    private final UserRepository userRepository;
    private final CommitService commitService;

    @Transactional
    @Scheduled(cron = "0 15 0 1 * ?")   // 매 월 1일  00:15 실행
    public void saveMonthlyCommitHistory() {
        /**
         * 계산
         * 1일 0시15분 -> 이전달 1일 0시 15분으로 이동 (minusMonth)
         * -> 1일로 설정 (withDayOfMonth) -> 일단위 자르기 (truncatedTo) 이를 통해 0시 0분 00초가 됨
         * -> 한달후로 이동.  1일 0시 0분 00초가 됨  -> -1 나노초  말일 23시 59분 59.999초가 됨
         * 이를 통해 1일 0시부터 말일 끝까지 커밋을 카운트 가능.  스케줄러가 돌아가는 동안 놓칠 커밋 갯수를 걱정하지 않아도 됨
         */
        ZonedDateTime toDate = ZonedDateTime.now().minusMonths(1).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS).plusMonths(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                long monthlyCommits = commitService.getCommitCountForMonth(user, fromDate.getYear(), fromDate.getMonthValue());
                CommitHistory commitHistory = CommitHistory.builder()
                        .user(user)
                        .year(fromDate.getYear())
                        .month(fromDate.getMonthValue())
                        .commitCount(monthlyCommits)
                        .recordDate(ZonedDateTime.now())
                        .build();

                commitHistoryRepository.save(commitHistory);

            } catch (Exception e) {
                log.error("월간 커밋 기록 저장에 실패하였습니다. user: {}. Error: {}", user.getId(), e.getMessage());
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 15 0 * * MON")   // 매주 월요일 0시 15분 실행   toDate 계산 방식은 위와 같음
    public void saveWeeklyCommitHistory() {
        ZonedDateTime toDate = ZonedDateTime.now().with(DayOfWeek.MONDAY).minusWeeks(1).plusDays(6).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.minusDays(6).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                long weeklyCommits = commitService.getCommitCountForWeek(user, fromDate.getYear(), fromDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
                CommitHistory commitHistory = CommitHistory.builder()
                        .user(user)
                        .year(fromDate.getYear())
                        .week(fromDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
                        .commitCount(weeklyCommits)
                        .recordDate(ZonedDateTime.now())
                        .build();

                commitHistoryRepository.save(commitHistory);

            } catch (Exception e) {
                log.error("주간 커밋 기록 저장에 실패하였습니다. user: {}. Error: {}", user.getId(), e.getMessage());
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 30 0 * * *")  // 매일 0시 30분 실행
    public void saveDailyCommitHistory() {
        ZonedDateTime toDate = ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.minusDays(1).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                long dailyCommits = commitService.getCommitCountForDay(user, fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth());
                CommitHistory commitHistory = CommitHistory.builder()
                        .user(user)
                        .year(fromDate.getYear())
                        .month(fromDate.getMonthValue())
                        .day(fromDate.getDayOfMonth())
                        .commitCount(dailyCommits)
                        .recordDate(ZonedDateTime.now())
                        .build();

                commitHistoryRepository.save(commitHistory);

            } catch (Exception e) {
                log.error("일간 커밋 기록 저장에 실패하였습니다. user: {}. Error: {}", user.getId(), e.getMessage());
            }
        }

    }

}
