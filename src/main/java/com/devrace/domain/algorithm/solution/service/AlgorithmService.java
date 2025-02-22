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
import com.devrace.domain.category_visibility.entity.CategoryVisibility;
import com.devrace.domain.category_visibility.enums.CategoryType;
import com.devrace.domain.category_visibility.repository.CategoryVisibilityRepository;
import com.devrace.domain.event.AlgorithmSubmitEvent;
import com.devrace.domain.log.entity.Log;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
import com.devrace.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlgorithmService {

    private final AlgorithmRepository algorithmRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final CategoryVisibilityRepository categoryVisibilityRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final List<String> ALLOWED_DOMAINS = List.of(
            "https://school.programmers.co.kr",
            "https://www.acmicpc.net",
            "https://leetcode.com"
    );

    @Transactional
    public SubmitAlgorithmResponseDto submitAlgorithm(Long userId, SubmitAlgorithmDto submitAlgorithmDto) {
        validateAddress(submitAlgorithmDto.getAddress());

        User user = checkUser(userId);

        Long problemId = saveProblem(submitAlgorithmDto);

        Solution solution = Solution.builder()
                .description(submitAlgorithmDto.getDescription())
                .review(submitAlgorithmDto.getReview())
                .isPublic(submitAlgorithmDto.isPublic())
                .nickName(user.getNickname())
                .problemId(problemId)
                .user(user)
                .build();

        algorithmRepository.save(solution);

        applicationEventPublisher.publishEvent(new AlgorithmSubmitEvent(userId));

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

    @Transactional
    public EditAlgorithmResponseDto editAlgorithm(Long solutionId, Long userId, EditAlgorithmDto editAlgorithmDto) {
        validateAddress(editAlgorithmDto.getAddress());
        User user = checkUser(userId);
        Long problemId = saveProblemEdit(editAlgorithmDto);
        Solution solution = checkSolution(solutionId, userId);

        solution.update(
                editAlgorithmDto.getDescription(),
                editAlgorithmDto.getReview(),
                problemId
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

    @Transactional
    public void changeAlgorithmVisibility(Long userId, Long solutionId) {
        checkUser(userId);
        Solution solution = checkSolution(solutionId);

        if (!solution.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        solution.changeIsPublic();
        algorithmRepository.save(solution);
    }

    public AlgorithmResponseDto getAlgorithm(Long solutionId, Long userId) {
        boolean isLogIn = userId != null;

        Solution solution = checkSolution(solutionId);
        User user = checkUser(solution.getUser().getId());
        Problem problem = problemRepository.findById(solution.getProblemId())
                .orElseThrow(() -> new CustomException(ErrorCode.PROBLEM_NOT_FOUND));

        boolean isOwner = isLogIn && solution.getUser().getId().equals(userId);

        CategoryVisibility categoryVisibility = getCategoryVisibility(solution.getUser().getId());

        if (!categoryVisibility.isPublic() && !isOwner) {
            throw new CustomException(ErrorCode.CATEGORY_IS_PRIVATE);
        }

        if (!solution.isPublic() && !isOwner) {
            throw new CustomException(ErrorCode.ALGORITHM_IS_PRIVATE);
        }

        return AlgorithmResponseDto.builder()
                .solutionId(solutionId)
                .title(problem.getTitle())
                .createdAt(solution.getCreatedAt())
                .description(solution.getDescription())
                .review(solution.getReview())
                .isPublic(solution.isPublic())
                .nickName(user.getNickname())
                .imageUrl(user.getImageUrl())
/**                .levelBadgeImageUrl(user.getUserLevel().getLevelBadgeImageUrl())   */
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

    private Solution checkSolution(Long solutionId) {
        return algorithmRepository.findById(solutionId)
                .orElseThrow(() -> new CustomException((ErrorCode.SOLUTION_NOT_FOUND)));
    }

    private CategoryVisibility getCategoryVisibility(Long userId) {
        CategoryVisibility categoryVisibility = categoryVisibilityRepository.findByUserIdAndType(userId, CategoryType.ALGORITHM)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_VISIBILITY_NOT_FOUND));
        return categoryVisibility;
    }
}
