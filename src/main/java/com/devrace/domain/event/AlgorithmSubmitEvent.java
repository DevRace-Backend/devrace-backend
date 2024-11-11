package com.devrace.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgorithmSubmitEvent {

    private final Long userId;
}
