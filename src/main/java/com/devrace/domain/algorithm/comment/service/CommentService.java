package com.devrace.domain.algorithm.comment.service;

import com.devrace.domain.algorithm.comment.controller.dto.CommentResponseDto;
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
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AlgorithmRepository algorithmRepository;

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

    @Transactional
    public List<CommentResponseDto> getCommentList(Long solutionId, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new CustomException(ErrorCode.INVALID_PAGE_OR_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> commentPage = commentRepository.findBySolutionIdOrderByCreatedAtDesc(solutionId, pageable);

        if (commentPage.isEmpty() && page >= commentPage.getTotalPages()) {
            throw new CustomException(ErrorCode.PAGE_NOT_FOUND);
        }

        commentPage.getContent().forEach(comment -> Hibernate.initialize(comment.getUser()));

        return commentPage.getContent().stream()
                .map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .nickName(comment.getUser().getNickname())
                        .profileImage(comment.getUser().getImageUrl())
/**                        .levelImage() */
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();
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
