package com.devrace.domain.algorithm.problem.repository;

import com.devrace.domain.algorithm.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findByAddressAndTitle(String address, String title);
}
