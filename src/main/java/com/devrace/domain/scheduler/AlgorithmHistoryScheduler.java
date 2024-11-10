package com.devrace.domain.scheduler;

import com.devrace.domain.algorithm.solution.entity.AlgorithmHistory;
import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.algorithm.solution.repository.AlgorithmCountRepository;
import com.devrace.domain.algorithm.solution.repository.AlgorithmHistoryRepository;
import com.devrace.domain.algorithm.solution.repository.AlgorithmRepository;
import com.devrace.domain.algorithm.solution.service.AlgorithmCountService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import com.sun.jdi.event.ExceptionEvent;
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
public class AlgorithmHistoryScheduler {

    private final AlgorithmCountService algorithmCountService;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmHistoryRepository algorithmHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 25 0 1 * ?")
    public void saveMonthlyAlgorithmHistory() {
        ZonedDateTime toDate = ZonedDateTime.now(ZoneOffset.UTC).minusMonths(1).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS).plusMonths(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                Solution solution = algorithmRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.SOLUTION_NOT_FOUND));

                long monthlySolutions = algorithmCountService.getAlgorithmCountForMonth(user, fromDate.getYear(), fromDate.getMonthValue());
                AlgorithmHistory algorithmHistory = AlgorithmHistory.builder()
                        .year(fromDate.getYear())
                        .month(fromDate.getMonthValue())
                        .solutionCount(monthlySolutions)
                        .recordDate(ZonedDateTime.now())
                        .solution(solution)
                        .build();

                algorithmHistoryRepository.save(algorithmHistory);

            } catch (Exception e) {
                log.error("월간 알고리즘 제출 기록 저장에 실패했습니다. user: {}, Error: {}", user.getId(), e.getMessage());
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 25 0 * * MON")
    public void saveWeeklyAlgorithmHistory() {
        ZonedDateTime toDate = ZonedDateTime.now().with(DayOfWeek.MONDAY).minusWeeks(1).plusDays(6).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.minusDays(6).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                Solution solution = algorithmRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.SOLUTION_NOT_FOUND));

                long weeklySolutions = algorithmCountService.getAlgorithmCountForWeek(user, fromDate.getYear(), fromDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
                AlgorithmHistory algorithmHistory = AlgorithmHistory.builder()
                        .year(fromDate.getYear())
                        .week(fromDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
                        .solutionCount(weeklySolutions)
                        .recordDate(ZonedDateTime.now())
                        .solution(solution)
                        .build();

                algorithmHistoryRepository.save(algorithmHistory);

            } catch (Exception e) {
                log.error("주간 알고리즘 제출 기록 저장에 실패했습니다. user: {}, Error: {}", user.getId(), e.getMessage());
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 40 0 * * *")
    public void saveDailyAlgorithmHistory() {
        ZonedDateTime toDate = ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1);
        ZonedDateTime fromDate = toDate.minusDays(1).truncatedTo(ChronoUnit.DAYS);

        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            try {
                Solution solution = algorithmRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.SOLUTION_NOT_FOUND));

                long dailySolutions = algorithmCountService.getAlgorithmCountForDay(user, fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth());
                AlgorithmHistory algorithmHistory = AlgorithmHistory.builder()
                        .year(fromDate.getYear())
                        .month(fromDate.getMonthValue())
                        .day(fromDate.getDayOfMonth())
                        .solutionCount(dailySolutions)
                        .recordDate(ZonedDateTime.now())
                        .solution(solution)
                        .build();

                algorithmHistoryRepository.save(algorithmHistory);

            } catch (Exception e) {
                log.error("일간 알고리즘 제출 기록 저장에 실패했습니다. user: {}, Error: {}", user.getId(), e.getMessage());
            }
        }
    }

}
