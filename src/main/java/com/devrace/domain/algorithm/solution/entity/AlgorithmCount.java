package com.devrace.domain.algorithm.solution.entity;

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
public class AlgorithmCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long totalSolutions;
    private long monthlySolutions;
    private long weeklySolutions;
    private long dailySolutions;
    private long continuousDays;

    private ZonedDateTime submitDate;
    private ZonedDateTime recentlyUpdatedTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", nullable = false, unique = true)
    private Solution solution;

    public void addTotalSolutions(long newSolutions) {
        this.totalSolutions += newSolutions;
    }

    public void updateMonthlySolutions(long newSolutions, boolean isNewMonth) {
        this.monthlySolutions = isNewMonth ? newSolutions : this.monthlySolutions + newSolutions;
    }

    public void updateWeeklySolutions(long newSolutions, boolean isNewWeek) {
        this.weeklySolutions = isNewWeek ? newSolutions : this.weeklySolutions + newSolutions;
    }

    public void updateDailySolutions(long newSolutions, boolean isNewDay) {
        this.dailySolutions = isNewDay ? newSolutions : this.dailySolutions + newSolutions;
    }

    public void updateContinuousDays(boolean isContinuous) {
        this.continuousDays = isContinuous ? this.continuousDays +1 : 0;

    }
    public void setRecentlyUpdateTime() {
        this.recentlyUpdatedTime = ZonedDateTime.now(ZoneOffset.UTC);
    }

}
