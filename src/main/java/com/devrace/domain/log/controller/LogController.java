package com.devrace.domain.log.controller;

import com.devrace.domain.log.controller.dto.SubmitLogDto;
import com.devrace.domain.log.controller.dto.SubmitLogResponseDto;
import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.service.LogService;
import com.devrace.domain.user.entity.User;
import com.devrace.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/log")
public class LogController {

    private final LogService logService;

    @PostMapping
    public ResponseEntity<SubmitLogResponseDto> submitLog(
            @AuthenticationPrincipal User user,
            @RequestBody SubmitLogDto submitLogDto) {

        if (logService.isDuplicatedLink(submitLogDto.getAddress(), user.getId())) {
            SubmitLogResponseDto errorResponse = SubmitLogResponseDto.builder()
                    .status("error")
                    .message(ErrorCode.DUPLICATED_LINK.getMessage())
                    .build();

            return ResponseEntity.status(ErrorCode.DUPLICATED_LINK.getHttpStatus())
                    .body(errorResponse);
        }

        Log log = Log.builder()
                .address(submitLogDto.getAddress())
                .title(submitLogDto.getTitle())
                .content(submitLogDto.getContent())
                .isPublic(true) // 나중에 변경
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        Log submitLog = logService.submitLog(log);

        SubmitLogResponseDto successResponse = SubmitLogResponseDto.builder()
                .status("성공")
                .message("성공적으로 제출되었습니다.")
                .logId(submitLog.getId())
                .createdAt(submitLog.getCreatedAt())
                .address(submitLog.getAddress())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(successResponse);
    }

}
