package com.devrace.domain.user.service;

import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.generator.NicknameGenerator;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.config.oauth.provider.OAuth2UserInfo;
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

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByPrimaryEmail(email);
    }

    private String getUniqueNickname(String nickname) {
        while (userRepository.existsByNickname(nickname)) {
            nickname = NicknameGenerator.generateRandomNickname();
        }
        return nickname;
    }
}
