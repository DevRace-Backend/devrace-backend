package com.devrace.domain.social_account.service;

import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.domain.social_account.repository.SocialAccountRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;

    public SocialAccount createSocialAccount(OAuth2UserInfo userInfo, User user) {
        return socialAccountRepository.save(SocialAccount.create(userInfo, user));
    }
}
