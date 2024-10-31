package com.devrace.domain.user.service;

import static com.devrace.global.exception.ErrorCode.ALREADY_EXIST_NICKNAME;
import static com.devrace.global.exception.ErrorCode.USER_NOT_FOUND;

import com.devrace.domain.user.controller.dto.request.BlogAddressUpdateRequest;
import com.devrace.domain.user.controller.dto.request.DescriptionUpdateRequest;
import com.devrace.domain.user.controller.dto.request.NicknameUpdateRequest;
import com.devrace.domain.user.controller.dto.response.MyInfoResponse;
import com.devrace.domain.user.controller.dto.response.UserInfoResponse;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.generator.NicknameGenerator;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import com.devrace.global.exception.CustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(OAuth2UserInfo userInfo) {
        String uniqueNickname = getUniqueNickname(userInfo.getName());
        return userRepository.save(User.create(userInfo, uniqueNickname));
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByPrimaryEmail(email);
    }

    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(Long userId) {
        User user = getUserById(userId);
        return MyInfoResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
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

    private String getUniqueNickname(String nickname) {
        while (isNotUniqueNickname(nickname)) {
            nickname = NicknameGenerator.generateRandomNickname();
        }
        return nickname;
    }

    @Transactional(readOnly = true)
    private boolean isNotUniqueNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
