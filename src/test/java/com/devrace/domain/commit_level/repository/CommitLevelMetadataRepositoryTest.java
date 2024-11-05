package com.devrace.domain.commit_level.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devrace.domain.commit_level.entity.CommitLevelMetadata;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class CommitLevelMetadataRepositoryTest {

    @Autowired
    private CommitLevelMetadataRepository commitLevelMetadataRepository;

    @Test
    @DisplayName("findByTotalCommits(전체 커밋 개수): 전체 커밋 개수에 해당되는 커밋 레벨을 조회한다.")
    void findByTotalCommits() {
        // given
        final CommitLevelMetadata commitLevelMetadata1 = new CommitLevelMetadata("새싹", 0, "임시URL");
        final CommitLevelMetadata commitLevelMetadata2 = new CommitLevelMetadata("잔디", 100, "임시URL");
        final CommitLevelMetadata commitLevelMetadata3 = new CommitLevelMetadata("나뭇가지", 1000, "임시URL");
        final CommitLevelMetadata commitLevelMetadata4 = new CommitLevelMetadata("큰 나무", 3000, "임시URL");
        final CommitLevelMetadata commitLevelMetadata5 = new CommitLevelMetadata("열매 달린 나무", 10000, "임시URL");
        commitLevelMetadataRepository.saveAll(List.of(commitLevelMetadata1, commitLevelMetadata2, commitLevelMetadata3, commitLevelMetadata4, commitLevelMetadata5));
        final long totalCommits = 100;

        // when
        CommitLevelMetadata result = commitLevelMetadataRepository.findFirstByTotalCommits(totalCommits);

        // then
        assertThat(result).extracting("name", "requirement", "imageUrl")
                .containsExactly(commitLevelMetadata2.getName(), commitLevelMetadata2.getRequirement(), commitLevelMetadata2.getImageUrl());
    }
}