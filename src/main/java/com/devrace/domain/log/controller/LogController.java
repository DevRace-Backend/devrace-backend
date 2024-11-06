package com.devrace.domain.log.controller;

import com.devrace.domain.log.controller.dto.EditLogDto;
import com.devrace.domain.log.controller.dto.EditLogResponseDto;
import com.devrace.domain.log.controller.dto.LogResponseDto;
import com.devrace.domain.log.controller.dto.SubmitLogDto;
import com.devrace.domain.log.controller.dto.SubmitLogResponseDto;
import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.service.LogService;
import com.devrace.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
public class LogController {

    private final LogService logService;

    @PostMapping
    public ResponseEntity<SubmitLogResponseDto> submitLog(
            @AuthenticationPrincipal Long userId,
            @RequestBody SubmitLogDto submitLogDto) {

        if (logService.isDuplicatedLink(submitLogDto.getAddress(), userId)) {
            SubmitLogResponseDto errorResponse = SubmitLogResponseDto.builder()
                    .status("error")
                    .message(ErrorCode.DUPLICATED_LINK.getMessage())
                    .build();

            return ResponseEntity.status(ErrorCode.DUPLICATED_LINK.getHttpStatus())
                    .body(errorResponse);
        }

        SubmitLogResponseDto successResponse = logService.submitLog(submitLogDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @PatchMapping("/edit/{logId}")
    public ResponseEntity<EditLogResponseDto> editLog(
            @PathVariable Long logId,
            @AuthenticationPrincipal Long userId,
            @RequestBody EditLogDto editLogDto
    ) {

        if (logService.isDuplicatedLink(editLogDto.getAddress(), userId)) {
            EditLogResponseDto errorResponse = EditLogResponseDto.builder()
                    .status("error")
                    .message(ErrorCode.DUPLICATED_LINK.getMessage())
                    .build();

            return ResponseEntity.status(ErrorCode.DUPLICATED_LINK.getHttpStatus())
                    .body(errorResponse);
        }

        EditLogResponseDto successResponse = logService.editLog(logId, editLogDto, userId);

        EditLogResponseDto.builder()
                .status("성공")
                .message("성공적으로 수정되었습니다.")
                .logId(logId)
                .address(editLogDto.getAddress())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(successResponse);

    }

    @GetMapping("/{logId}")
    public ResponseEntity<LogResponseDto> getLog(
            @PathVariable Long logId
    ) {
        Log log = logService.getLogById(logId);
        LogResponseDto responseDto = logService.createLogResponse(log);
        return ResponseEntity.ok(responseDto);
    }
}
