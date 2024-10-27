package com.devrace.domain.social_account.repository;

import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.global.config.oauth.provider.ProviderType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    @Query("SELECT sa FROM SocialAccount sa JOIN FETCH sa.user WHERE sa.providerType = :providerType AND sa.providerId = :providerId")
    Optional<SocialAccount> findByProviderTypeAndProviderId(ProviderType providerType, String providerId);
}
