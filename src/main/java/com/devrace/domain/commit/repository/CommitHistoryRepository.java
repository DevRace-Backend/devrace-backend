package com.devrace.domain.commit.repository;

import com.devrace.domain.commit.entity.CommitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitHistoryRepository extends JpaRepository<CommitHistory, Long> {

}

