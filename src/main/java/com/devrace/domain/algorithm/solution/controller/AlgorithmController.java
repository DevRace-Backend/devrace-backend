package com.devrace.domain.algorithm.solution.controller;

import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentResponseDto;
import com.devrace.domain.algorithm.solution.controller.dto.AlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.controller.dto.EditAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.EditAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.service.AlgorithmService;
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
@RequestMapping("/api/v1/algorithm")
public class AlgorithmController {

    private final AlgorithmService algorithmService;

    @Operation(summary = "알고리즘 제출", description = "알고리즘 제출 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "알고리즘을 제출하였습니다.",
                    content = @Content(schema = @Schema(implementation = SubmitAlgorithmResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 링크입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<SubmitAlgorithmResponseDto> submitAlgorithm(
            @AuthenticationPrincipal Long userId,
            @RequestBody SubmitAlgorithmDto submitAlgorithmDto
    ) {
        SubmitAlgorithmResponseDto responseDto = algorithmService.submitAlgorithm(userId, submitAlgorithmDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "알고리즘 제출 수정", description = "알고리즘 제출 수정 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "알고리즘 제출 수정이 완료되었습니다.",
                    content = @Content(schema = @Schema(implementation = EditAlgorithmResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 링크입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/edit/{solutionId}")
    public ResponseEntity<EditAlgorithmResponseDto> editAlgorithm(
            @Parameter(description = "수정할 솔루션ID", required = true) @PathVariable Long solutionId,
            @AuthenticationPrincipal Long userId,
            @RequestBody EditAlgorithmDto editAlgorithmDto
    ) {

        EditAlgorithmResponseDto responseDto = algorithmService.editAlgorithm(solutionId, userId, editAlgorithmDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "알고리즘 공개비공개 변경", description = "알고리즘 공개비공개 변경 API")
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
                    description = "유저를 찾을 수 없습니다. 또는 제출된 알고리즘 풀이를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/edit/{solutionId}/visible")
    public ResponseEntity<Void> changeSolutionVisibility(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long solutionId
    ) {
        algorithmService.changeAlgorithmVisibility(userId, solutionId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "알고리즘 단건 조회", description = "알고리즘 단건 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "알고리즘 조회에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = AlgorithmResponseDto.class))),

            @ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없습니다. 또는 제출된 알고리즘 풀이를 찾을 수 없습니다. 또는 알고리즘 문제를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{solutionId}")
    public ResponseEntity<AlgorithmResponseDto> getAlgorithm(
            @Parameter(description = "조회할 솔루션ID", required = true) @PathVariable Long solutionId) {

        AlgorithmResponseDto responseDto = algorithmService.getAlgorithm(solutionId);

        return ResponseEntity.ok(responseDto);
    }
}
