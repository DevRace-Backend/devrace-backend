package com.devrace.domain.commit.repository;

import com.devrace.domain.commit.entity.CommitCount;
import com.devrace.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommitCountRepository extends JpaRepository<CommitCount, Long> {
    Optional<CommitCount> findByUser(User user);
}
