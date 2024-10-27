package com.devrace.domain.log.service;

import com.devrace.domain.log.entity.Log;
import com.devrace.domain.log.repository.LogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional
    public Log submitLog(Log log) {
        return logRepository.save(log);
    }

    public boolean isDuplicatedLink(String address, Long userId) {
        return logRepository.findByAddressAndUserId(address, userId).isPresent();
    }
}
