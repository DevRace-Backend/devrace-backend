package com.devrace.domain.social_account.service;

import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.domain.social_account.repository.SocialAccountRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.config.oauth.provider.ProviderType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;

    @Transactional
    public SocialAccount createSocialAccount(OAuth2UserInfo userInfo, User user) {
        return socialAccountRepository.save(SocialAccount.create(userInfo, user));
    }

    public Optional<SocialAccount> findSocialAccountByProvider(ProviderType providerType, String providerId) {
        return socialAccountRepository.findByProviderTypeAndProviderId(providerType, providerId);
    }
}
