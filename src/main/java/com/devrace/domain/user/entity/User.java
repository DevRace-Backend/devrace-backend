package com.devrace.domain.user.entity;

import com.devrace.domain.commit.entity.CommitCount;
import com.devrace.domain.commit_level.entity.CommitLevel;
import com.devrace.domain.follow.entity.Follower;
import com.devrace.domain.follow.entity.Following;
import com.devrace.domain.user.enums.UserRole;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users", indexes = @Index(name = "idx_nickname", columnList = "nickname"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 21, nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(length = 200)
    private String description;

    @Column(unique = true)
    private String primaryEmail;

    @Column
    private String imageUrl;

    @Column(unique = true)
    private String githubName;

    @Column
    private String blogAddress;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column
    private ZonedDateTime deletedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CommitCount commitCount;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CommitLevel commitLevel;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follower> followerList = new ArrayList<Follower>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Following> followingList = new ArrayList<Following>();

    public static User create(OAuth2UserInfo userInfo, String uniqueNickname) {
        return new User(
                uniqueNickname,
                userInfo.getEmail(),
                userInfo.getImageUrl(),
                userInfo.isGithub(),
                userInfo.getName());
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeBlogAddress(String blogAddress) {
        this.blogAddress = blogAddress;
    }

    public User(String nickname, String primaryEmail, String imageUrl, boolean isGithub, String githubName) {
        this.nickname = nickname;
        this.role = UserRole.USER;
        this.primaryEmail = primaryEmail;
        this.imageUrl = imageUrl;
        this.githubName = isGithub ? githubName : null;
    }
}
