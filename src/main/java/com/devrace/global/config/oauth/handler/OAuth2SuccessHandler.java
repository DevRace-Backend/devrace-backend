package com.devrace.global.config.oauth.handler;

import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.domain.social_account.repository.SocialAccountRepository;
import com.devrace.domain.social_account.service.SocialAccountService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.domain.user.service.UserService;
import com.devrace.global.config.oauth.PrincipalUser;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final UserRepository userRepository;
    private final SocialAccountService socialAccountService;
    private final SocialAccountRepository socialAccountRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        OAuth2UserInfo userInfo = principalUser.getUserInfo();

        User user = getUser(userInfo);

    }

    private User getUser(OAuth2UserInfo userInfo) {
        return socialAccountRepository
                .findByProviderTypeAndProviderId(userInfo.getProviderType(), userInfo.getProviderId())
                .map(SocialAccount::getUser)
                .orElseGet(() -> {
                    User user = userRepository.findByPrimaryEmail(userInfo.getEmail())
                            .orElseGet(() -> userService.createUser(userInfo));

                    socialAccountService.createSocialAccount(userInfo, user);
                    return user;
                });
    }
}
