package com.devrace.domain.log.controller;

import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmResponseDto;
import com.devrace.domain.log.controller.dto.EditLogDto;
import com.devrace.domain.log.controller.dto.EditLogResponseDto;
import com.devrace.domain.log.controller.dto.LogResponseDto;
import com.devrace.domain.log.controller.dto.SubmitLogDto;
import com.devrace.domain.log.controller.dto.SubmitLogResponseDto;
import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.service.LogService;
import com.devrace.domain.user.entity.User;
import com.devrace.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponse;
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

    @Operation(summary = "개발일지(로그) 제출", description = "개발일지(로그) 제출 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "개발일지(로그)를 제출하였습니다.",
                    content = @Content(schema = @Schema(implementation = SubmitLogResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "중복된 링크입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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

    @Operation(summary = "개발일지(로그) 수정", description = "개발일지(로그) 수정 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "개발일지(로그)를 수정하였습니다.",
                    content = @Content(schema = @Schema(implementation = SubmitLogResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "중복된 링크입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다. 또는 로그를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/edit/{logId}")
    public ResponseEntity<EditLogResponseDto> editLog(
            @Parameter(description = "수정할 로그ID", required = true) @PathVariable Long logId,
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

    @Operation(summary = "개발일지(로그) 공개비공개 변경", description = "개발일지(로그) 공개비공개 변경 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "No Content"),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다. 또는 로그를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/edit/{logId}/visible")
    public ResponseEntity<Void> changeLogVisibility(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long logId
    ) {
        logService.changeLogVisibility(userId, logId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "개발일지(로그) 단건 조회", description = "개발일지(로그) 단건 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "개발일지(로그)를 조회하였습니다.",
                    content = @Content(schema = @Schema(implementation = LogResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "중복된 링크입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "로그를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{logId}")
    public ResponseEntity<LogResponseDto> getLog(
            @PathVariable Long logId
    ) {
        Log log = logService.getLogById(logId);
        LogResponseDto responseDto = logService.createLogResponse(log);
        return ResponseEntity.ok(responseDto);
    }
}
