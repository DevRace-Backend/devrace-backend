package com.devrace.domain.algorithm.comment.controller;

import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentDto;
import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentResponseDto;
import com.devrace.domain.algorithm.comment.controller.dto.EditCommentDto;
import com.devrace.domain.algorithm.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/algorithm/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{solutionId}")
    public ResponseEntity<CreateCommentResponseDto> createComment(
            @PathVariable Long solutionId,
            @AuthenticationPrincipal Long userId,
            @RequestBody CreateCommentDto createCommentDto
    ) {
        CreateCommentResponseDto responseDto = commentService.createComment(solutionId, userId, createCommentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

//    @PatchMapping("/{commentId}")
//    public ResponseEntity<editCommentResponseDto> editComment(
//            @PathVariable Long commentId,
//            @AuthenticationPrincipal Long userId,
//            @RequestBody EditCommentDto editCommentDto
//    ) {
//
//    }
}
