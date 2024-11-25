package com.devrace.domain.log.service;

import com.devrace.domain.category_visibility.entity.CategoryVisibility;
import com.devrace.domain.category_visibility.enums.CategoryType;
import com.devrace.domain.category_visibility.repository.CategoryVisibilityRepository;
import com.devrace.domain.event.LogSubmitEvent;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final CategoryVisibilityRepository categoryVisibilityRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public SubmitLogResponseDto submitLog(SubmitLogDto submitLogDto, Long userId) {

        User user = checkUser(userId);

        Log log = Log.builder()
                .address(submitLogDto.getAddress())
                .title(submitLogDto.getTitle())
                .content(submitLogDto.getContent())
                .isPublic(submitLogDto.isPublic())
                .user(user)
                .build();

        logRepository.save(log);

        applicationEventPublisher.publishEvent(new LogSubmitEvent(userId));

        return SubmitLogResponseDto.builder()
                .status("성공")
                .message("성공적으로 제출되었습니다.")
                .logId(log.getId())
                .createdAt(log.getCreatedAt())
                .address(log.getAddress())
                .isPublic(log.isPublic())
                .build();
    }

    @Transactional
    public EditLogResponseDto editLog(Long logId, EditLogDto editLogDto, Long userId) {
        Log log = checkLog(logId, userId);
        User user = checkUser(userId);

        log.editLog(editLogDto);

        logRepository.save(log);

        return EditLogResponseDto.builder()
                .status("성공")
                .message("성공적으로 수정되었습니다.")
                .logId(logId)
                .address(log.getAddress())
                .isPublic(log.isPublic())
                .build();
    }

    @Transactional
    public void changeLogVisibility(Long userId, Long logId) {
        checkUser(userId);
        Log log = checkLog(logId);

        if (!log.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        log.changeIsPublic();
        logRepository.save(log);
    }

    public Log getLogById(Long logId, Long userId) {
        checkUser(userId);
        Log log = checkLog(logId);

        boolean isOwner = log.getUser().getId().equals(userId);

        CategoryVisibility categoryVisibility = getCategoryVisibility(log.getUser().getId());

        if (!categoryVisibility.isPublic() && !isOwner) {
            throw new CustomException(ErrorCode.CATEGORY_IS_PRIVATE);
        }

        if (!log.isPublic() && !isOwner) {
            throw new CustomException(ErrorCode.LOG_IS_PRIVATE);
        }

        return log;
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

    private Log checkLog(Long logId) {

        return logRepository.findById(logId)
                .orElseThrow(() -> new CustomException((ErrorCode.LOG_NOT_FOUND)));
    }

    private CategoryVisibility getCategoryVisibility(Long userId) {
        CategoryVisibility categoryVisibility = categoryVisibilityRepository.findByUserIdAndType(userId, CategoryType.LOG)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_VISIBILITY_NOT_FOUND));
        return categoryVisibility;
    }

    public boolean isDuplicatedLink(String address, Long userId) {
        return logRepository.findByAddressAndUserId(address, userId).isPresent();
    }
}
