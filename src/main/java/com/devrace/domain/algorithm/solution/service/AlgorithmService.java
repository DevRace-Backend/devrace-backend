package com.devrace.domain.algorithm.solution.service;

import com.devrace.domain.algorithm.problem.entity.Problem;
import com.devrace.domain.algorithm.problem.repository.ProblemRepository;
import com.devrace.domain.algorithm.solution.controller.dto.AlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.controller.dto.EditAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.EditAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.algorithm.solution.repository.AlgorithmRepository;
import com.devrace.domain.log.entity.Log;
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
                .description(solution.getDescription())
                .review(solution.getReview())
                .isPublic(solution.isPublic())
                .createdAt(solution.getCreatedAt())
                .build();
    }

    public EditAlgorithmResponseDto editAlgorithm(Long solutionId, Long userId, EditAlgorithmDto editAlgorithmDto) {
        validateAddress(editAlgorithmDto.getAddress());
        User user = checkUser(userId);
        Long problemId = saveProblemEdit(editAlgorithmDto);
        Solution solution = checkSolution(solutionId, userId);

        solution.update(
                editAlgorithmDto.getDescription(),
                editAlgorithmDto.getReview(),
                problemId,
                editAlgorithmDto.isPublic()
        );

        algorithmRepository.save(solution);

        return EditAlgorithmResponseDto.builder()
                .id(solution.getId())
                .nickName(user.getNickname())
                .address(editAlgorithmDto.getAddress())
                .title(editAlgorithmDto.getTitle())
                .description(solution.getDescription())
                .review(solution.getReview())
                .isPublic(solution.isPublic())
                .build();
    }

    public AlgorithmResponseDto getAlgorithm(Long solutionId, Long userId) {
        Solution solution = checkSolution(solutionId, userId);
        User user = checkUser(userId);
        Problem problem = problemRepository.findById(solution.getProblemId())
                .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));

        return AlgorithmResponseDto.builder()
                .solutionId(solutionId)
                .title(problem.getTitle())
                .createdAt(solution.getCreatedAt())
                .description(solution.getDescription())
                .review(solution.getReview())
                .isPublic(solution.isPublic())
                .nickName(user.getNickname())
                .imageUrl(user.getImageUrl())
//                .levelBadgeImageUrl(user.getUserLevel().getLevelBadgeImageUrl())
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

    private Long saveProblemEdit(EditAlgorithmDto editAlgorithmDto) {
        return problemRepository.findByAddressAndTitle(
                        editAlgorithmDto.getAddress(),
                        editAlgorithmDto.getTitle())
                .orElseGet(() -> problemRepository.save(
                        new Problem(editAlgorithmDto.getAddress(), editAlgorithmDto.getTitle())
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

    private Solution checkSolution(Long solutionId, Long userId) {
        return algorithmRepository.findByIdAndUserId(solutionId, userId)
                .orElseThrow(() -> new CustomException((ErrorCode.SOLUTION_NOT_FOUND)));
    }
}
