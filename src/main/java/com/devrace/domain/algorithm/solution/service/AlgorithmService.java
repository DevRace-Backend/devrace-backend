package com.devrace.domain.algorithm.solution.service;

import com.devrace.domain.algorithm.problem.entity.Problem;
import com.devrace.domain.algorithm.problem.repository.ProblemRepository;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.algorithm.solution.repository.AlgorithmRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlgorithmService {

    private final AlgorithmRepository algorithmRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;

    private static final List<String> ALLOWED_DOMAINS = List.of(
            "https://school.programmers.co.kr",
            "https://www.acmicpc.net",
            "https://leetcode.com"
    );

    public SubmitAlgorithmResponseDto submitAlgorithm(Long userId, SubmitAlgorithmDto submitAlgorithmDto) {
        validateAddress(submitAlgorithmDto.getAddress());

        User user = checkUser(userId);

        Long problemId = saveProblem(submitAlgorithmDto);

        Solution solution = Solution.builder()
                .description(submitAlgorithmDto.getDescription())
                .review(submitAlgorithmDto.getReview())
                .createdAt(LocalDateTime.now())
                .isPublic(submitAlgorithmDto.isPublic())
                .userId(userId)
                .nickName(user.getNickname())
                .problemId(problemId)
                .build();

        algorithmRepository.save(solution);

        return SubmitAlgorithmResponseDto.builder()
                .id(solution.getId())
                .nickName(solution.getNickName())
                .address(submitAlgorithmDto.getAddress())
                .title(submitAlgorithmDto.getTitle())
                .content(solution.getDescription())
                .review(solution.getReview())
                .isPublic(solution.isPublic())
                .createdAt(solution.getCreatedAt())
                .build();
    }

    private Long saveProblem(SubmitAlgorithmDto submitAlgorithmDto) {
        return problemRepository.findByAddressAndTitle(
                        submitAlgorithmDto.getAddress(),
                        submitAlgorithmDto.getTitle())
                .orElseGet(() -> problemRepository.save(
                        new Problem(submitAlgorithmDto.getAddress(), submitAlgorithmDto.getTitle())
                )).getId();
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException((ErrorCode.USER_NOT_FOUND)));
    }

    private void validateAddress(String address) {
        boolean isValid =
                ALLOWED_DOMAINS.stream().anyMatch(address::startsWith);
        if (!isValid) {
            throw new CustomException(ErrorCode.INVALID_LINK);
        }
    }
}
