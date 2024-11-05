package com.devrace.domain.commit_level.repository;

import com.devrace.domain.commit_level.entity.CommitLevelMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommitLevelMetadataRepository extends JpaRepository<CommitLevelMetadata,Long> {
    @Query("SELECT clm FROM CommitLevelMetadata clm WHERE clm.requirement <= :totalCommits ORDER BY clm.requirement DESC LIMIT 1")
    CommitLevelMetadata findFirstByTotalCommits(long totalCommits);
}
