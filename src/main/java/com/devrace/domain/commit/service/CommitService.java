package com.devrace.domain.commit.service;

import com.devrace.domain.commit.component.GithubApiClient;
import com.devrace.domain.commit.entity.Commit;
import com.devrace.domain.commit.entity.CommitCount;
import com.devrace.domain.commit.repository.CommitCountRepository;
import com.devrace.domain.commit.repository.CommitRepository;
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
public class CommitService {

    private final GithubApiClient githubApiClient;
    private final CommitRepository commitRepository;
    private final CommitCountRepository commitCountRepository;
    private final UserRepository userRepository;

    /**
     * 커밋 데이터 수집 및 저장
     */
    @Transactional
    public void collectCommitData(Long userId, ZonedDateTime toDate) {
        User user = checkUser(userId);

        // 회원가입 시간을 fromdate로 저장
        ZonedDateTime fromDate = user.getCreatedAt();

        String githubName = user.getGithubName();

        if (githubName == null || githubName.isEmpty()) {
            throw new CustomException(ErrorCode.GITHUB_NOT_FOUND);
        }

        long newCommits = getCommitCountFromGithub(githubName, fromDate, toDate);

        if (newCommits > 0) {
            // 커밋 갱신
            updateCommit(user, toDate, newCommits);

            // 커밋 카운트 갱신
            updateCommitCount(user, toDate, newCommits);

        }

    }

    /**
     * github api를 사용하여 커밋 수를 가져옴
     */
    private long getCommitCountFromGithub(String githubName, ZonedDateTime fromDate, ZonedDateTime toDate) {
        try {
            return githubApiClient.getCommitCountInperiod(githubName, fromDate, toDate);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_TO_CONNECT_GITHUB);
        }
    }

    /**
     * 커밋 갱신   깃허브로부터 정보를 받아와서 갱신
     * <p>
     * 커밋이 없는 경우 null 반환을 방지하며, 새로운 커밋 객체를 생성하여 커밋 수 0으로 설정
     */
    private void updateCommit(User user, ZonedDateTime toDate, long newCommits) {
        Commit commit = commitRepository.findByUserAndCommitDate(user, toDate)
                .orElse(new Commit(null, toDate, 0, user));
        commit.addCommit(commit.getCommitCount() + newCommits);
        commitRepository.save(commit);
    }

    private void updateCommitCount(User user, ZonedDateTime toDate, long newCommits) {
        CommitCount commitCount = commitCountRepository.findByUser(user)
                .orElseGet(() -> {
                    CommitCount createCommitCount = CommitCount.builder()
                            .user(user)
                            .totalCommits(0)
                            .monthlyCommits(0)
                            .weeklyCommits(0)
                            .dailyCommits(0)
                            .recentlyUpdateTime(ZonedDateTime.now())
                            .build();
                    return commitCountRepository.save(createCommitCount);
                });

        boolean isNewMonth = !isSameMonth(commitCount.getRecentlyUpdateTime(), toDate);
        if (isNewMonth) {
            commitCount.updateMonthlyCommits(newCommits, true);
        } else {
            commitCount.updateMonthlyCommits(newCommits, false);
        }

        boolean isNewWeek = !isSameWeek(commitCount.getRecentlyUpdateTime(), toDate);
        if (isNewWeek) {
            commitCount.updateWeeklyCommits(newCommits, true);
        } else {
            commitCount.updateWeeklyCommits(newCommits, false);
        }

        boolean isNewDay = !isSameDay(commitCount.getRecentlyUpdateTime(), toDate);
        if (isNewDay) {
            commitCount.updateDailyCommits(newCommits, true);
        } else {
            commitCount.updateDailyCommits(newCommits, false);
        }

        commitCount.updateTotalCommits(commitCount.getTotalCommits() + newCommits);
        commitCount.setRecentlyUpdateTime(toDate);
    }

    private boolean isSameMonth(ZonedDateTime recentlyUpdateTime, ZonedDateTime toDate) {
        return recentlyUpdateTime.getYear() == toDate.getYear() && recentlyUpdateTime.getMonth() == toDate.getMonth();
    }

    // 주간 확인.  연도와 주간(월~일)이 같은지 확인   시분초는 비교에 필요없으므로 제거
    private boolean isSameWeek(ZonedDateTime recentlyUpdateTime, ZonedDateTime toDate) {
        ZonedDateTime startWeek1 = recentlyUpdateTime.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime startWeek2 = toDate.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        return recentlyUpdateTime.getYear() == toDate.getYear() && startWeek1.equals(startWeek2);
    }

    private boolean isSameDay(ZonedDateTime recentlyUpdateTime, ZonedDateTime toDate) {
        return recentlyUpdateTime.truncatedTo(ChronoUnit.DAYS)
                .isEqual(toDate.truncatedTo(ChronoUnit.DAYS));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    /**
     * 원하는 월, 주, 일에 해당하는 커밋을 가져오는 로직
     */

    public long getCommitCountForMonth(User user, int year, int month) {
        ZonedDateTime startDate = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = startDate.plusMonths(1).minusNanos(1);
        return commitRepository.countByUserAndCommitDateBetween(user, startDate, endDate);
    }

    public long getCommitCountForWeek(User user, int year, int week) {
        ZonedDateTime startDate = ZonedDateTime.now()
                .withYear(year)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                .with(DayOfWeek.MONDAY);
        ZonedDateTime endDate = startDate.plusWeeks(1).minusNanos(1);
        return commitRepository.countByUserAndCommitDateBetween(user, startDate, endDate);
    }

    public long getCommitCountForDay(User user, int year, int month, int day) {
        ZonedDateTime startDate = ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime endDate = startDate.plusDays(1).minusNanos(1);
        return commitRepository.countByUserAndCommitDateBetween(user, startDate, endDate);
    }


}
