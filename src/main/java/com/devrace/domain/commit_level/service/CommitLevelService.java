package com.devrace.domain.commit_level.service;

import com.devrace.domain.commit_level.entity.CommitLevel;
import com.devrace.domain.commit_level.entity.CommitLevelMetadata;
import com.devrace.domain.commit_level.repository.CommitLevelMetadataRepository;
import com.devrace.domain.commit_level.repository.CommitLevelRepository;
import com.devrace.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommitLevelService {

    private final UserService userService;
    private final CommitLevelRepository commitLevelRepository;
    private final CommitLevelMetadataRepository commitLevelMetadataRepository;

    public void updateOrCreateCommitLevel(Long userId, long newTotalCommits) {
        CommitLevelMetadata commitLevelMetadata = commitLevelMetadataRepository.findFirstByTotalCommits(newTotalCommits);
        commitLevelRepository.findByUserId(userId)
                .ifPresentOrElse(commitLevel -> updateCommitLevel(commitLevel, commitLevelMetadata),
                        () -> createCommitLevel(userId, commitLevelMetadata));
    }

    @Transactional
    private void updateCommitLevel(CommitLevel commitLevel, CommitLevelMetadata commitLevelMetadata) {
        if (isChangedLevel(commitLevel, commitLevelMetadata)) {
            commitLevel.changeLevel(commitLevelMetadata);
        }
    }

    @Transactional
    private CommitLevel createCommitLevel(Long userId, CommitLevelMetadata commitLevelMetadata) {
        return commitLevelRepository.save(CommitLevel.create(commitLevelMetadata, userService.getUserById(userId)));
    }

    private boolean isChangedLevel(CommitLevel commitLevel, CommitLevelMetadata commitLevelMetadata) {
        return !commitLevel.getLevelName().equals(commitLevelMetadata.getName());
    }
}
