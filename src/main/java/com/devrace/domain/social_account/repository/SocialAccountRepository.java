package com.devrace.domain.social_account.repository;

import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.global.config.oauth.provider.ProviderType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);
}
