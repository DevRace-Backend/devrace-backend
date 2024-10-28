package com.devrace.domain.core.service;

import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.domain.social_account.service.SocialAccountService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.service.UserService;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSocialAccountService {

    private final UserService userService;
    private final SocialAccountService socialAccountService;

    public User getUser(OAuth2UserInfo userInfo) {
        return socialAccountService.findSocialAccountByProvider(userInfo.getProviderType(), userInfo.getProviderId())
                .map(SocialAccount::getUser)
                .orElseGet(() -> {
                    User user = userService.findUserByEmail(userInfo.getEmail())
                            .orElseGet(() -> userService.createUser(userInfo));

                    socialAccountService.createSocialAccount(userInfo, user);
                    return user;
                });
    }
}
