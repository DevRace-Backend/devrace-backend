package com.devrace.domain.commit.controllerTEST;

import com.devrace.domain.commit.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/commit")
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;

    @PostMapping("/{userId}")
    public String commitTest(@PathVariable Long userId) {
        ZonedDateTime toDate = ZonedDateTime.now();

        try {
            commitService.collectCommitData(userId, toDate);
            return "이거 성공했냐" + userId;
        } catch (Exception e) {
            return "이거 실패임" + e.getMessage();
        }
    }

}
