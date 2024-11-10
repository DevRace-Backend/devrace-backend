package com.devrace.domain.algorithm.solution.entity;

import com.devrace.domain.user.entity.User;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Solution extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(length = 300)
    private String review;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private String nickName;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Solution(User user, String description, String review, boolean isPublic,  String nickName, Long problemId) {
        this.user = user;
        this.description = description;
        this.review = review;
        this.isPublic = isPublic;
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
