package com.devrace.domain.algorithm.comment.service;

import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentDto;
import com.devrace.domain.algorithm.comment.controller.dto.CreateCommentResponseDto;
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

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException((ErrorCode.USER_NOT_FOUND)));
    }


    private Solution checkSolution(Long solutionId, Long userId) {
        return algorithmRepository.findByIdAndUserId(solutionId, userId)
                .orElseThrow(() -> new CustomException((ErrorCode.SOLUTION_NOT_FOUND)));
    }
}
