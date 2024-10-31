package com.devrace.domain.algorithm.comment.service;

import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentDto;
import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentResponseDto;
import com.devrace.domain.algorithm.comment.controller.dto.EditCommentDto;
import com.devrace.domain.algorithm.comment.controller.dto.EditCommentResponseDto;
import com.devrace.domain.algorithm.comment.entity.Comment;
import com.devrace.domain.algorithm.comment.repository.CommentRepository;
import com.devrace.domain.algorithm.problem.repository.ProblemRepository;
import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.algorithm.solution.repository.AlgorithmRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AlgorithmRepository algorithmRepository;
    private final ProblemRepository problemRepository;

    @Transactional
    public CreateCommentResponseDto createComment(Long solutionId, Long userId, CreateCommentDto createCommentDto) {
        Solution solution = checkSolution(solutionId, userId);
        User user = checkUser(userId);

        Comment comment = Comment.builder()
                .content(createCommentDto.getContent())
                .user(user)
                .solution(solution)
                .build();

        commentRepository.save(comment);

        return CreateCommentResponseDto.builder()
                .id(comment.getId())
                .nickName(user.getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Transactional
    public EditCommentResponseDto editComment(Long commentId, Long userId, EditCommentDto editCommentDto) {
        Comment comment = checkComment(commentId);

        validateUser(userId, comment);

        comment.updateContent(editCommentDto.getContent());

        commentRepository.save(comment);

        return EditCommentResponseDto.builder()
                .id(commentId)
                .nickName(comment.getUser().getNickname())
                .content(comment.getContent())
                .build();
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = checkComment(commentId);
        validateUser(userId, comment);

        commentRepository.delete(comment);
    }

    private static void validateUser(Long userId, Comment comment) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException((ErrorCode.USER_NOT_FOUND)));
    }

    private Solution checkSolution(Long solutionId, Long userId) {
        return algorithmRepository.findByIdAndUserId(solutionId, userId)
                .orElseThrow(() -> new CustomException((ErrorCode.SOLUTION_NOT_FOUND)));
    }

    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

}
