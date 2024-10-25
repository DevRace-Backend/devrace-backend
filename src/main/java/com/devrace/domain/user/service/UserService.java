package com.devrace.domain.user.service;

import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.generator.NicknameGenerator;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(OAuth2UserInfo userInfo) {
        String uniqueNickname = getUniqueNickname(userInfo.getName());
        return userRepository.save(User.create(userInfo, uniqueNickname));
    }


    private String getUniqueNickname(String nickname) {
        while (userRepository.existsByNickname(nickname)) {
            nickname = NicknameGenerator.generateRandomNickname();
        }
        return nickname;
    }
}
