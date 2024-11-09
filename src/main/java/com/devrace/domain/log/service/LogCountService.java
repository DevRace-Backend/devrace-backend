package com.devrace.domain.log.service;

import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.entity.LogCount;
import com.devrace.domain.log.repository.LogCountRepository;
import com.devrace.domain.log.repository.LogHistoryRepository;
import com.devrace.domain.log.repository.LogRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;

@Service
@RequiredArgsConstructor
public class LogCountService {

    private final LogRepository logRepository;
    private final LogCountRepository logCountRepository;
    private final LogHistoryRepository logHistoryRepository;
    private final UserRepository userRepository;


    @Transactional
    public void updateLogCountOnSubmit(Long userId) {
        User user = checkUser(userId);
        ZonedDateTime toDate = ZonedDateTime.now(ZoneOffset.UTC);

        LogCount logCount = logCountRepository.findByUser(user)
                .orElseGet(() -> {
                    Log log = getLog(userId);
                    LogCount createLogCount = LogCount.builder()
                            .totalLogs(0)
                            .monthlyLogs(0)
                            .weeklyLogs(0)
                            .dailyLogs(0)
                            .continuousDays(0)
                            .recentlyupdatedTime(ZonedDateTime.now(ZoneOffset.UTC))
                            .log(log)
                            .build();
                    return logCountRepository.save(createLogCount);
                });

        logCount.addTotalLogs(1);
        logCount.updateMonthlyLogs(1, isNewMonth(logCount.getRecentlyupdatedTime(), toDate));
        logCount.updateWeeklyLogs(1, isNewWeek(logCount.getRecentlyupdatedTime(), toDate));
        logCount.updateDailyLogs(1, isNewDay(logCount.getRecentlyupdatedTime(), toDate));

        boolean isContinuous = !isNewDay(logCount.getRecentlyupdatedTime(), toDate);
        logCount.updateContinuousDays(isContinuous);

        logCount.setRecentlyUpdateTime();

        logCountRepository.save(logCount);
    }


    private Log getLog(Long userId) {
        return logRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.LOG_NOT_FOUND));
    }

    public long getLogCountForMonth(User user, int year, int month) {
        ZonedDateTime startDate = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = startDate.plusMonths(1).minusNanos(1);
        return logCountRepository.countByUserAndCountDateBetween(user, startDate, endDate);
    }

    public long getLogCountForWeek(User user, int year, int week) {
        ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC)
                .withYear(year)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                .with(DayOfWeek.MONDAY);
        ZonedDateTime endDate = startDate.plusWeeks(1).minusNanos(1);
        return logCountRepository.countByUserAndCountDateBetween(user, startDate, endDate);
    }

    public long getLogCountForDay(User user, int year, int month, int day) {
        ZonedDateTime startDate = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = startDate.plusDays(1).minusNanos(1);
        return logCountRepository.countByUserAndCountDateBetween(user, startDate, endDate);
    }
    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private boolean isNewMonth(ZonedDateTime recentlyUpdateTime, ZonedDateTime toDate) {
        return recentlyUpdateTime.getYear() != toDate.getYear()
                || recentlyUpdateTime.getMonth() != toDate.getMonth();
    }

    // 주간 확인.  연도와 주간(월~일)이 다른지 확인(시분초는 비교에 필요없으므로 제거)
    private boolean isNewWeek(ZonedDateTime recentlyUpdateTime, ZonedDateTime toDate) {
        ZonedDateTime startWeek1 = recentlyUpdateTime.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime startWeek2 = toDate.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        return recentlyUpdateTime.getYear() != toDate.getYear()
                || !startWeek1.equals(startWeek2);
    }

    private boolean isNewDay(ZonedDateTime recentlyUpdateTime, ZonedDateTime toDate) {
        return !recentlyUpdateTime.truncatedTo(ChronoUnit.DAYS)
                .equals(toDate.truncatedTo(ChronoUnit.DAYS));
    }

}
