package com.devrace.domain.event;

import com.devrace.domain.log.service.LogCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LogCountListener {

    private final LogCountService logCountService;

    @TransactionalEventListener
    @Async
    public void handleLogSubmitEvent(LogSubmitEvent event) {
        logCountService.updateLogCountOnSubmit(event.getUserId());
    }
}
