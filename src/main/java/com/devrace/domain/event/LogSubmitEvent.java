package com.devrace.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogSubmitEvent {

    private final Long userId;

}
