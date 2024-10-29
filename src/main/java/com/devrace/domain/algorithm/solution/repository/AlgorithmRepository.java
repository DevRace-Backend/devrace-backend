package com.devrace.domain.algorithm.solution.repository;

import com.devrace.domain.algorithm.solution.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmRepository extends JpaRepository<Solution, Long> {

}
