package com.devrace.domain.log.repository;

import com.devrace.domain.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    Optional<Log> findByAddressAndUserId(String address, Long userId);

    Optional<Log> findByIdAndUserId(Long logId, Long userId);

    Optional<Log> findByUserId(Long id);
}
