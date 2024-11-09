package com.devrace.domain.log.repository;

import com.devrace.domain.log.entity.LogHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogHistoryRepository extends JpaRepository<LogHistory, Long> {

}
