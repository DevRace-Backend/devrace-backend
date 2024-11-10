package com.devrace.domain.algorithm.solution.service;

import com.devrace.domain.algorithm.solution.entity.AlgorithmCount;
import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.algorithm.solution.repository.AlgorithmCountRepository;
import com.devrace.domain.algorithm.solution.repository.AlgorithmHistoryRepository;
import com.devrace.domain.algorithm.solution.repository.AlgorithmRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;

@Service
@RequiredArgsConstructor
public class AlgorithmCountService {

    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmCountRepository algorithmCountRepository;
    private final AlgorithmHistoryRepository algorithmHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updateAlgorithmCountOnSubmit(Long userId) {
        User user = checkUser(userId);
        ZonedDateTime toDate = ZonedDateTime.now(ZoneOffset.UTC);

        AlgorithmCount algorithmCount = algorithmCountRepository.findByUser(user)
                .orElseGet(() -> {
                    Solution solution = getSolution(userId);
                    AlgorithmCount createAlgorithmCount = AlgorithmCount.builder()
                            .totalSolutions(0)
                            .monthlySolutions(0)
                            .weeklySolutions(0)
                            .dailySolutions(0)
                            .continuousDays(0)
                            .recentlyUpdatedTime(ZonedDateTime.now(ZoneOffset.UTC))
                            .solution(solution)
                            .user(user)
                            .build();
                    return algorithmCountRepository.save(createAlgorithmCount);
                });

        algorithmCount.addTotalSolutions(1);
        algorithmCount.updateMonthlySolutions(1, isNewMonth(algorithmCount.getRecentlyUpdatedTime(), toDate));
        algorithmCount.updateWeeklySolutions(1, isNewWeek(algorithmCount.getRecentlyUpdatedTime(), toDate));
        algorithmCount.updateDailySolutions(1, isNewDay(algorithmCount.getRecentlyUpdatedTime(), toDate));

        boolean isContinuous = !isNewDay(algorithmCount.getRecentlyUpdatedTime(), toDate);
        algorithmCount.updateContinuousDays(isContinuous);

        algorithmCount.setRecentlyUpdateTime();

        algorithmCountRepository.save(algorithmCount);
    }

    private Solution getSolution(Long userId) {
        return algorithmRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.SOLUTION_NOT_FOUND));
    }
    public long getAlgorithmCountForMonth(User user, int year, int month) {
        ZonedDateTime startDate = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = startDate.plusMonths(1).minusNanos(1);
        return algorithmCountRepository.countByUserAndCountDateBetween(user, startDate, endDate);
    }

    public long getAlgorithmCountForWeek(User user, int year, int week) {
        ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC)
                .withYear(year)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                .with(DayOfWeek.MONDAY);
        ZonedDateTime endDate = startDate.plusWeeks(1).minusNanos(1);
        return algorithmCountRepository.countByUserAndCountDateBetween(user, startDate, endDate);
    }

    public long getAlgorithmCountForDay(User user, int year, int month, int day) {
        ZonedDateTime startDate = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = startDate.plusDays(1).minusNanos(1);
        return algorithmCountRepository.countByUserAndCountDateBetween(user, startDate, endDate);
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
