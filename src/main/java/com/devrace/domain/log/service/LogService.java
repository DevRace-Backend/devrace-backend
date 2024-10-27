package com.devrace.domain.log.service;

import com.devrace.domain.log.controller.dto.SubmitLogDto;
import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.repository.LogRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;

    @Transactional
    public Log submitLog(SubmitLogDto submitLogDto, Long userId) {

        User user = checkUser(userId);

        Log log = Log.builder()
                .address(submitLogDto.getAddress())
                .title(submitLogDto.getTitle())
                .content(submitLogDto.getContent())
                .isPublic(true) // 나중에 변경
                .createdAt(LocalDateTime.now())
                .userId(user.getId())
                .build();

        return logRepository.save(log);
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException((ErrorCode.USER_NOT_FOUND)));
    }

    public boolean isDuplicatedLink(String address, Long userId) {
        return logRepository.findByAddressAndUserId(address, userId).isPresent();
    }
}
