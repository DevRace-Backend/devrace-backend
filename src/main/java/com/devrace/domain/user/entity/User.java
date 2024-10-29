package com.devrace.domain.user.entity;

import com.devrace.domain.user.enums.UserRole;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 63, nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(length = 600)
    private String description;

    @Column
    private String primaryEmail;

    @Column
    private String imageUrl;

    @Column
    private String githubName;

    @Column
    private String blogAddress;

    @Column
    private boolean isDeleted;

    @Column
    private ZonedDateTime deletedAt;

    public static User create(OAuth2UserInfo userInfo, String uniqueNickname) {
        return new User(
                uniqueNickname,
                userInfo.getEmail(),
                userInfo.getImageUrl(),
                userInfo.isGithub(),
                userInfo.getName());
    }

    public User(String nickname, String primaryEmail, String imageUrl, boolean isGithub, String githubName) {
        this.nickname = nickname;
        this.role = UserRole.USER;
        this.primaryEmail = primaryEmail;
        this.imageUrl = imageUrl;
        this.githubName = isGithub ? githubName : null;
    }
}
