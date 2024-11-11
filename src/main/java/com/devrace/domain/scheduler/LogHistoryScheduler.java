package com.devrace.domain.scheduler;

import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.entity.LogCount;
import com.devrace.domain.log.entity.LogHistory;
import com.devrace.domain.log.repository.LogCountRepository;
import com.devrace.domain.log.repository.LogHistoryRepository;
import com.devrace.domain.log.repository.LogRepository;
import com.devrace.domain.log.service.LogCountService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogHistoryScheduler {

    private final LogCountService logCountService;
    private final LogRepository logRepository;
    private final LogHistoryRepository logHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 20 0 1 * ?")
    public void saveMonthlyLogHistory() {
        ZonedDateTime toDate = ZonedDateTime.now(ZoneOffset.UTC).minusMonths(1).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS).plusMonths(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                Log log = logRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.LOG_NOT_FOUND));

                long monthlyLogs = logCountService.getLogCountForMonth(user, fromDate.getYear(), fromDate.getMonthValue());
                LogHistory logHistory = LogHistory.builder()
                        .year(fromDate.getYear())
                        .month(fromDate.getMonthValue())
                        .logCount(monthlyLogs)
                        .recordDate(ZonedDateTime.now())
                        .log(log)
                        .build();

                logHistoryRepository.save(logHistory);

            } catch (Exception e) {
                log.error("월간 로그 기록 저장에 실패했습니다. user: {}, Error: {}", user.getId(), e.getMessage());
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 20 0 * * MON")
    public void saveWeeklyLogHistory() {
        ZonedDateTime toDate = ZonedDateTime.now().with(DayOfWeek.MONDAY).minusWeeks(1).plusDays(6).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.minusDays(6).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                Log log = logRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.LOG_NOT_FOUND));

                long weeklyLogs = logCountService.getLogCountForWeek(user, fromDate.getYear(), fromDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
                LogHistory logHistory = LogHistory.builder()
                        .year(fromDate.getYear())
                        .week(fromDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
                        .logCount(weeklyLogs)
                        .recordDate(ZonedDateTime.now())
                        .log(log)
                        .build();

                logHistoryRepository.save(logHistory);

            } catch (Exception e) {
                log.error("주간 로그 기록 저장에 실패했습니다. user: {}, Error: {}", user.getId(), e.getMessage());
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 35 0 * * *")
    public void saveDailyLogHistory() {
        ZonedDateTime toDate = ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.minusDays(1).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                Log log = logRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.LOG_NOT_FOUND));

                long dailyLogs = logCountService.getLogCountForDay(user, fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth());
                LogHistory logHistory = LogHistory.builder()
                        .year(fromDate.getYear())
                        .month(fromDate.getMonthValue())
                        .day(fromDate.getDayOfMonth())
                        .logCount(dailyLogs)
                        .recordDate(ZonedDateTime.now())
                        .log(log)
                        .build();

                logHistoryRepository.save(logHistory);

            } catch (Exception e) {
                log.error("일간 로그 기록 저장에 실패했습니다. user: {}, Error: {}", user.getId(), e.getMessage());
            }
        }
    }
}
