package com.devrace.domain.commit.handler;

import com.devrace.domain.commit.event.CommitCountUpdatedEvent;
import com.devrace.domain.commit_level.service.CommitLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommitCountUpdatedHandler {

    private final CommitLevelService commitLevelService;

    @Async
    @EventListener(CommitCountUpdatedEvent.class)
    public void handler(CommitCountUpdatedEvent event) {
        commitLevelService.updateOrCreateCommitLevel(event.getUserId(), event.getNewTotalCommits());
    }
}
