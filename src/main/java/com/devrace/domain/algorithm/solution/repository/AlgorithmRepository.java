package com.devrace.domain.algorithm.solution.repository;

import com.devrace.domain.algorithm.solution.entity.Solution;
import com.devrace.domain.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlgorithmRepository extends JpaRepository<Solution, Long> {

    Optional<Solution> findByIdAndUserId(Long solutionId, Long userId);
}
