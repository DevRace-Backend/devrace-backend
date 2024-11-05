package com.devrace.domain.commit_level.repository;

import com.devrace.domain.commit_level.entity.CommitLevel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitLevelRepository extends JpaRepository<CommitLevel,Long> {
    Optional<CommitLevel> findByUserId(Long userId);
}
