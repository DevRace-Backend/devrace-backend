package com.devrace.domain.algorithm.solution.controller;

import com.devrace.domain.algorithm.solution.controller.dto.AlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.controller.dto.EditAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.EditAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmDto;
import com.devrace.domain.algorithm.solution.controller.dto.SubmitAlgorithmResponseDto;
import com.devrace.domain.algorithm.solution.service.AlgorithmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping
    public ResponseEntity<SubmitAlgorithmResponseDto> submitAlgorithm(
            @AuthenticationPrincipal Long userId,
            @RequestBody SubmitAlgorithmDto submitAlgorithmDto
    ) {
        SubmitAlgorithmResponseDto responseDto = algorithmService.submitAlgorithm(userId, submitAlgorithmDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/edit/{solutionId}")
    public ResponseEntity<EditAlgorithmResponseDto> editAlgorithm(
            @PathVariable Long solutionId,
            @AuthenticationPrincipal Long userId,
            @RequestBody EditAlgorithmDto editAlgorithmDto
    ) {

        EditAlgorithmResponseDto responseDto = algorithmService.editAlgorithm(solutionId, userId, editAlgorithmDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{solutionId}")
    public ResponseEntity<AlgorithmResponseDto> getAlgorithm(
            @PathVariable Long solutionId) {

        AlgorithmResponseDto responseDto = algorithmService.getAlgorithm(solutionId);

        return ResponseEntity.ok(responseDto);
    }
}
