package com.devrace.domain.log.entity;

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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long totalLogs;
    private long monthlyLogs;
    private long weeklyLogs;
    private long dailyLogs;
    private long continuousDays;

    private ZonedDateTime submitDate;
    private ZonedDateTime recentlyupdatedTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false, unique = true)
    private Log log;

    public void addTotalLogs(long newLogs) {
        this.totalLogs += newLogs;
    }

    public void updateMonthlyLogs(long newLogs, boolean isNewMonth) {
        this.monthlyLogs = isNewMonth ? newLogs : this.monthlyLogs + newLogs;
    }

    public void updateWeeklyLogs(long newLogs, boolean isNewWeek) {
        this.weeklyLogs = isNewWeek ? newLogs : this.weeklyLogs + newLogs;
    }

    public void updateDailyLogs(long newLogs, boolean isNewDay) {
        this.dailyLogs = isNewDay ? newLogs : this.dailyLogs + newLogs;
    }

    public void updateContinuousDays(boolean isContinuous) {
        this.continuousDays = isContinuous ? this.continuousDays +1 : 0;
    }

    public void setRecentlyUpdateTime() {
        this.recentlyupdatedTime = ZonedDateTime.now(ZoneOffset.UTC);
    }

}
