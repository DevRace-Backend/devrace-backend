package com.devrace.domain.algorithm.solution.repository;

import com.devrace.domain.algorithm.solution.entity.AlgorithmHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmHistoryRepository extends JpaRepository<AlgorithmHistory, Long> {


}
