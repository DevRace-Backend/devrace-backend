package com.devrace.domain.social_account.entity;

import com.devrace.domain.user.entity.User;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.config.oauth.provider.ProviderType;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_providerType_providerId", columnList = "providerType, providerId"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(nullable = false)
    private String providerId;

    @Column
    private String email;

    public static SocialAccount create(OAuth2UserInfo userInfo, User user) {
        return SocialAccount.builder()
                .user(user)
                .providerType(userInfo.getProviderType())
                .providerId(userInfo.getProviderId())
                .email(userInfo.getEmail())
                .build();
    }

    @Builder
    private SocialAccount(User user, ProviderType providerType, String providerId, String email) {
        this.user = user;
        this.providerType = providerType;
        this.providerId = providerId;
        this.email = email;
    }
}
