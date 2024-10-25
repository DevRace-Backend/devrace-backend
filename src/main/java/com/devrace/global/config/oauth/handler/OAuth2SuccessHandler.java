package com.devrace.global.config.oauth.handler;

import static com.devrace.global.config.oauth.repository.CookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_COOKIE_NAME;

import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.domain.social_account.repository.SocialAccountRepository;
import com.devrace.domain.social_account.service.SocialAccountService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.domain.user.service.UserService;
import com.devrace.global.config.oauth.PrincipalUser;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.config.oauth.repository.CookieOAuth2AuthorizationRequestRepository;
import com.devrace.global.config.jwt.JwtTokenProvider;
import com.devrace.global.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        OAuth2UserInfo userInfo = principalUser.getUserInfo();

        User user = getUser(userInfo);

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().getAuthority());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        addTokens(response, accessToken, refreshToken);

        String targetUrl = determineTargetUrl(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_COOKIE_NAME).map(Cookie::getValue);
        clearAuthenticationAttributes(request, response);
        return redirectUri.orElseGet(() -> getDefaultTargetUrl());
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        cookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
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

    private void addTokens(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, accessToken);
        CookieUtils.addRefreshToken(response, refreshToken);
    }
}
