package com.devrace.domain.commit.entity;

import com.devrace.domain.commit.event.CommitCountUpdatedEvent;
import com.devrace.domain.user.entity.User;
import com.devrace.global.event.Events;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private long totalCommits;
    private long monthlyCommits;
    private long weeklyCommits;
    private long dailyCommits;

    private ZonedDateTime recentlyUpdateTime;

    public void addTotalCommits(long newCommits) {
        this.totalCommits += newCommits;
        Events.raise(new CommitCountUpdatedEvent(user.getId(), this.totalCommits));
    }

    public void updateMonthlyCommits(long newCommits, boolean isNewMonth) {
        this.monthlyCommits = isNewMonth ? newCommits : this.monthlyCommits + newCommits;
    }

    public void updateWeeklyCommits(long newCommits, boolean isNewWeek) {
        this.weeklyCommits = isNewWeek ? newCommits : this.weeklyCommits + newCommits;
    }

    public void updateDailyCommits(long newCommits, boolean isNewDay) {
        this.dailyCommits = isNewDay ? newCommits : this.dailyCommits + newCommits;
    }

    public void setRecentlyUpdateTime(ZonedDateTime now) {
        this.recentlyUpdateTime = now;
    }
}
