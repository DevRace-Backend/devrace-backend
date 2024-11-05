package com.devrace.domain.commit_level.entity;

import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_requirement", columnList = "requirement"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommitLevelMetadata extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private int requirement;

    @Column(nullable = false)
    private String imageUrl;

    public CommitLevelMetadata(String name, int requirement, String imageUrl) {
        this.name = name;
        this.requirement = requirement;
        this.imageUrl = imageUrl;
    }
}
