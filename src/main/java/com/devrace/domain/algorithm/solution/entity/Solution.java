package com.devrace.domain.algorithm.solution.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(length = 300)
    private String review;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String nickName;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Builder
    public Solution(String description, String review, LocalDateTime createdAt, boolean isPublic, Long userId, String nickName, Long problemId) {
        this.description = description;
        this.review = review;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.userId = userId;
        this.nickName = nickName;
        this.problemId = problemId;
    }

    public void update(String description, String review, Long problemId, boolean isPublic) {
        this.description = description;
        this.review = review;
        this.problemId = problemId;
        this.isPublic = isPublic;
    }
}
