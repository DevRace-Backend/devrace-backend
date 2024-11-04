package com.devrace.domain.commit.repository;

import com.devrace.domain.commit.entity.Commit;
import com.devrace.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
    Optional<Commit> findByUserAndCommitDate(User user, ZonedDateTime commitDate);
    long countByUserAndCommitDateBetween(User user, ZonedDateTime startDate, ZonedDateTime endDate);
}
