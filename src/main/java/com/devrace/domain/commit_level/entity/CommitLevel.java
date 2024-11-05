package com.devrace.domain.commit_level.entity;

import com.devrace.domain.user.entity.User;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_user_id", columnList = "user_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommitLevel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String levelName;

    @Column(nullable = false)
    private String levelImageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public static CommitLevel create(CommitLevelMetadata commitLevelMetadata, User user) {
        return new CommitLevel(
                commitLevelMetadata.getName(),
                commitLevelMetadata.getImageUrl(),
                user);
    }

    public CommitLevel(String levelName, String levelImageUrl, User user) {
        this.levelName = levelName;
        this.levelImageUrl = levelImageUrl;
        this.user = user;
    }

    public void changeLevel(CommitLevelMetadata commitLevelMetadata) {
        this.levelName = commitLevelMetadata.getName();
        this.levelImageUrl = commitLevelMetadata.getImageUrl();
    }
}
