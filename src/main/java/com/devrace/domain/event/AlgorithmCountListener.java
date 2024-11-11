package com.devrace.domain.event;

import com.devrace.domain.algorithm.solution.service.AlgorithmCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlgorithmCountListener {

    private final AlgorithmCountService algorithmCountService;

    @TransactionalEventListener
    @Async
    public void handleAlgorithmSubmitEvent(AlgorithmSubmitEvent event) {
        algorithmCountService.updateAlgorithmCountOnSubmit(event.getUserId());
    }
}
