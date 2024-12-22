package com.devrace.domain.user.service;

import static com.devrace.global.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devrace.domain.category_visibility.service.CategoryVisibilityService;
import com.devrace.domain.social_account.entity.SocialAccount;
import com.devrace.domain.social_account.service.SocialAccountService;
import com.devrace.domain.user.controller.dto.request.BlogAddressUpdateRequest;
import com.devrace.domain.user.controller.dto.request.DescriptionUpdateRequest;
import com.devrace.domain.user.controller.dto.request.NicknameUpdateRequest;
import com.devrace.domain.user.controller.dto.response.LoginResponse;
import com.devrace.domain.user.controller.dto.response.MyInfoResponse;
import com.devrace.domain.user.controller.dto.response.UserInfoResponse;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.generator.NicknameGenerator;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.config.jwt.JwtTokenProvider;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.config.oauth.provider.ProviderType;
import com.devrace.global.exception.CustomException;
import com.devrace.global.util.CookieUtils;
import com.devrace.global.util.SocialLoginService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SocialLoginService socialLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialAccountService socialAccountService;
    private final CategoryVisibilityService categoryVisibilityService;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByPrimaryEmail(email);
    }

    public MyInfoResponse getMyInfo(Long userId) {
        User user = getUserById(userId);
        return MyInfoResponse.from(user);
    }

    public UserInfoResponse getUserInfo(String nickname) {
        User user = getUserByNickname(nickname);
        return UserInfoResponse.from(user);
    }

    @Transactional
    public void updateNickname(Long userId, NicknameUpdateRequest request) {
        User user = getUserById(userId);

        if (isNotUniqueNickname(request.getNickname())) {
            throw new CustomException(ALREADY_EXIST_NICKNAME);
        }

        user.changeNickname(request.getNickname());
    }

    @Transactional
    public void updateDescription(Long userId, DescriptionUpdateRequest request) {
        User user = getUserById(userId);
        user.changeDescription(request.getDescription());
    }

    @Transactional
    public void updateBlogAddress(Long userId, BlogAddressUpdateRequest request) {
        User user = getUserById(userId);
        user.changeBlogAddress(request.getBlogAddress());
    }

    public LoginResponse login(ProviderType providerType, String code, HttpServletResponse response) {
        JsonNode jsonNode = socialLoginService.getOAuthResponse(providerType, code);

        String token = socialLoginService.extractToken(jsonNode);
        OAuth2UserInfo oAuth2UserInfo = socialLoginService.getUserInfo(providerType, token);

        User user = getUser(oAuth2UserInfo);
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().getAuthority());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        CookieUtils.addRefreshToken(response, refreshToken);
        return LoginResponse.from(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public User getUser(OAuth2UserInfo userInfo) {
        return socialAccountService.findSocialAccountByProvider(
                userInfo.getProviderType(), userInfo.getProviderId())
            .map(SocialAccount::getUser)
            .orElseGet(() -> {
                User user = findUserByEmail(userInfo.getEmail())
                    .orElseGet(() -> {
                        User newUser = userRepository.save(
                            User.create(userInfo, getUniqueNickname(userInfo.getName())));
                        categoryVisibilityService.createCategoryVisibility(newUser);
                        return newUser;
                    });

                socialAccountService.createSocialAccount(userInfo, user);
                return user;
            });
    }

    private String getUniqueNickname(String nickname) {
        while (isNotUniqueNickname(nickname)) {
            nickname = NicknameGenerator.generateRandomNickname();
        }
        return nickname;
    }

    @Transactional(readOnly = true)
    protected boolean isNotUniqueNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
