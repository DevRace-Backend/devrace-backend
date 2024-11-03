package com.devrace.domain.commit.controllerTEST;

import com.devrace.domain.scheduler.CommitScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sch")
@RequiredArgsConstructor
public class SchedulerControllerTEST {

    private final CommitScheduler commitScheduler;

    @PostMapping("/run")
    public String runSche() {
        commitScheduler.updateCommitScheduler();
        return "잘돌아감";
    }
}
