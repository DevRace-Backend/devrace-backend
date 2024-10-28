package com.devrace.global.config.oauth.handler;

import static com.devrace.global.config.oauth.repository.CookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_COOKIE_NAME;

import com.devrace.domain.core.service.UserSocialAccountService;
import com.devrace.domain.user.entity.User;
import com.devrace.global.config.jwt.JwtTokenProvider;
import com.devrace.global.config.oauth.PrincipalUser;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.config.oauth.repository.CookieOAuth2AuthorizationRequestRepository;
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

    private final UserSocialAccountService userSocialAccountService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        OAuth2UserInfo userInfo = principalUser.getUserInfo();
        User user = userSocialAccountService.getUser(userInfo);

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().getAuthority());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        addTokens(response, accessToken, refreshToken);

        String targetUrl = determineTargetUrl(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void addTokens(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, accessToken);
        CookieUtils.addRefreshToken(response, refreshToken);
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
}
