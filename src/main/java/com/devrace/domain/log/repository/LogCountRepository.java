package com.devrace.domain.log.repository;

import com.devrace.domain.log.entity.LogCount;
import com.devrace.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface LogCountRepository extends JpaRepository<LogCount, Long> {

    long countByUserAndCountDateBetween(User user, ZonedDateTime startDate, ZonedDateTime endDate);

    Optional<LogCount> findByUser(User user);
}
