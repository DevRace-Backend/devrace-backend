package com.devrace.domain.algorithm.comment.controller;

import com.devrace.domain.algorithm.comment.controller.dto.CommentResponseDto;
import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentDto;
import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentResponseDto;
import com.devrace.domain.algorithm.comment.controller.dto.EditCommentDto;
import com.devrace.domain.algorithm.comment.controller.dto.EditCommentResponseDto;
import com.devrace.domain.algorithm.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/algorithm/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "댓글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글을 작성하였습니다.",
                    content = @Content(schema = @Schema(implementation = CreateCommentResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "제출된 알고리즘 풀이를 찾을 수 없습니다. 또는 유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/{solutionId}")
    public ResponseEntity<CreateCommentResponseDto> createComment(
            @Parameter(description = "댓글을 추가할 솔루션ID", required = true) @PathVariable Long solutionId,
            @AuthenticationPrincipal Long userId,
            @RequestBody CreateCommentDto createCommentDto
    ) {
        CreateCommentResponseDto responseDto = commentService.createComment(solutionId, userId, createCommentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글을 수정하였습니다.",
                    content = @Content(schema = @Schema(implementation = EditCommentResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "댓글을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않았습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<EditCommentResponseDto> editComment(
            @Parameter(description = "댓글을 수정할 솔루션ID", required = true) @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId,
            @RequestBody EditCommentDto editCommentDto
    ) {
        EditCommentResponseDto responseDto = commentService.editComment(commentId, userId, editCommentDto);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글을 삭제하였습니다.",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "댓글을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않았습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "삭제할 댓글ID", required = true) @PathVariable Long commentId,
            @AuthenticationPrincipal Long userId
    ) {
        commentService.deleteComment(commentId, userId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 조회", description = "댓글 조회 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 리스트 조회",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class)))),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 페이지입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "페이지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("{solutionId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(
            @Parameter(description = "댓글을 조회할 솔루션ID", required = true) @PathVariable Long solutionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {

        List<CommentResponseDto> responseDtoList = commentService.getCommentList(solutionId, page, size);

        return ResponseEntity.ok(responseDtoList);
    }
}
