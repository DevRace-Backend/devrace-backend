package com.devrace.domain.core.service;

import com.devrace.domain.category_visibility.service.CategoryVisibilityService;
import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.domain.social_account.service.SocialAccountService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.service.UserService;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSocialCategoryService {

    private final UserService userService;
    private final SocialAccountService socialAccountService;
    private final CategoryVisibilityService categoryVisibilityService;

    public User getUser(OAuth2UserInfo userInfo) {
        return socialAccountService.findSocialAccountByProvider(userInfo.getProviderType(), userInfo.getProviderId())
                .map(SocialAccount::getUser)
                .orElseGet(() -> {
                    User user = userService.findUserByEmail(userInfo.getEmail())
                            .orElseGet(() -> {
                                User newUser = userService.createUser(userInfo);
                                categoryVisibilityService.createCategoryVisibility(newUser);
                                return newUser;
                            });

                    socialAccountService.createSocialAccount(userInfo, user);
                    return user;
                });
    }
}
