package com.devrace.domain.algorithm.solution.repository;

import com.devrace.domain.algorithm.solution.entity.AlgorithmCount;
import com.devrace.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface AlgorithmCountRepository extends JpaRepository<AlgorithmCount, Long> {


    long countByUserAndCountDateBetween(User user, ZonedDateTime startDate, ZonedDateTime endDate);

    Optional<AlgorithmCount> findByUser(User user);
}
