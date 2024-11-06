package com.devrace.domain.log.service;

import com.devrace.domain.log.controller.dto.EditLogDto;
import com.devrace.domain.log.controller.dto.EditLogResponseDto;
import com.devrace.domain.log.controller.dto.LogResponseDto;
import com.devrace.domain.log.controller.dto.SubmitLogDto;
import com.devrace.domain.log.controller.dto.SubmitLogResponseDto;
import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.repository.LogRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubmitLogResponseDto submitLog(SubmitLogDto submitLogDto, Long userId) {

        User user = checkUser(userId);

        Log log = Log.builder()
                .address(submitLogDto.getAddress())
                .title(submitLogDto.getTitle())
                .content(submitLogDto.getContent())
                .isPublic(true) // 나중에 변경
                .userId(user.getId())
                .build();

        logRepository.save(log);

        return SubmitLogResponseDto.builder()
                .status("성공")
                .message("성공적으로 제출되었습니다.")
                .logId(log.getId())
                .createdAt(log.getCreatedAt())
                .address(log.getAddress())
                .build();
    }

    @Transactional
    public EditLogResponseDto editLog(Long logId, EditLogDto editLogDto, Long userId) {
        Log log = checkLog(logId, userId);

        log = Log.builder()
                .address(editLogDto.getAddress())
                .title(editLogDto.getTitle())
                .content(editLogDto.getContent())
                .isPublic(log.isPublic())
                .userId(userId)
                .build();

        logRepository.save(log);

        return EditLogResponseDto.builder()
                .status("성공")
                .message("성공적으로 수정되었습니다.")
                .logId(logId)
                .address(log.getAddress())
                .build();
    }

    public Log getLogById(Long logId) {
        return logRepository.findById(logId)
                .orElseThrow(() -> new CustomException((ErrorCode.LOG_NOT_FOUND)));
    }
    public LogResponseDto createLogResponse(Log log) {
        return LogResponseDto.builder()
                .logId(log.getId())
                .address(log.getAddress())
                .createdAt(log.getCreatedAt())
                .isPublic(log.isPublic())
                .build();
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException((ErrorCode.USER_NOT_FOUND)));
    }

    private Log checkLog(Long logId, Long userId) {

        return logRepository.findByIdAndUserId(logId, userId)
                .orElseThrow(() -> new CustomException((ErrorCode.LOG_NOT_FOUND)));
    }

    public boolean isDuplicatedLink(String address, Long userId) {
        return logRepository.findByAddressAndUserId(address, userId).isPresent();
    }
}
