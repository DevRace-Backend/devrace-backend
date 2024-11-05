package com.devrace.domain.commit.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommitCountUpdatedEvent {

    private Long userId;
    private long newTotalCommits;
}
