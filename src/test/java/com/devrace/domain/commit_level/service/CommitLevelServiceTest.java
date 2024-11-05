package com.devrace.domain.commit_level.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.devrace.domain.commit_level.entity.CommitLevel;
import com.devrace.domain.commit_level.entity.CommitLevelMetadata;
import com.devrace.domain.commit_level.repository.CommitLevelMetadataRepository;
import com.devrace.domain.commit_level.repository.CommitLevelRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class CommitLevelServiceTest {

    @Autowired
    private CommitLevelService commitLevelService;

    @Autowired
    private CommitLevelRepository commitLevelRepository;

    @Autowired
    private CommitLevelMetadataRepository commitLevelMetadataRepository;

    @Autowired
    private UserRepository userRepository;

    private CommitLevelMetadata commitLevelMetadata1;
    private CommitLevelMetadata commitLevelMetadata2;
    private CommitLevelMetadata commitLevelMetadata3;
    private CommitLevelMetadata commitLevelMetadata4;
    private CommitLevelMetadata commitLevelMetadata5;
    private User user;

    @BeforeEach
    void setUp() {
        commitLevelMetadata1 = new CommitLevelMetadata("새싹", 0, "임시URL");
        commitLevelMetadata2 = new CommitLevelMetadata("잔디", 100, "임시URL");
        commitLevelMetadata3 = new CommitLevelMetadata("나뭇가지", 1000, "임시URL");
        commitLevelMetadata4 = new CommitLevelMetadata("큰 나무", 3000, "임시URL");
        commitLevelMetadata5 = new CommitLevelMetadata("열매 달린 나무", 10000, "임시URL");
        commitLevelMetadataRepository.saveAll(List.of(commitLevelMetadata1, commitLevelMetadata2, commitLevelMetadata3, commitLevelMetadata4, commitLevelMetadata5));

        user = new User("Hut234", "test@gmail.com", "http://imageUrl.com", true, "tester");
        userRepository.save(user);
    }

    @Test
    @DisplayName("updateOrCreateCommitLevel(유저 PK, 업데이트된 커밋 총 개수): 기존에 CommitLevel이 존재하지 않으면 새로 생성한다.")
    void updateOrCreateCommitLevel_create_success() {
        // given
        final long newTotalCommits = commitLevelMetadata2.getRequirement();

        // when
        commitLevelService.updateOrCreateCommitLevel(user.getId(), newTotalCommits);

        // then
        Optional<CommitLevel> result = commitLevelRepository.findByUserId(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).extracting("levelName", "levelImageUrl", "user")
                .containsExactly(commitLevelMetadata2.getName(), commitLevelMetadata2.getImageUrl(), user);
    }

    @Test
    @DisplayName("updateOrCreateCommitLevel(유저 PK, 업데이트된 커밋 총 개수): 기존 CommitLevel이 존재하고 레벨이 변경된 경우 업데이트한다.")
    void updateOrCreateCommitLevel_update_success() {
        // given
        final CommitLevel commitLevel = new CommitLevel("새싹", "임시URL", user);
        commitLevelRepository.save(commitLevel);

        final long newTotalCommits = commitLevelMetadata3.getRequirement();

        // when
        commitLevelService.updateOrCreateCommitLevel(user.getId(), newTotalCommits);

        // then
        Optional<CommitLevel> result = commitLevelRepository.findByUserId(user.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).extracting("levelName", "levelImageUrl", "user")
                .containsExactly(commitLevelMetadata3.getName(), commitLevelMetadata3.getImageUrl(), user);
    }
}