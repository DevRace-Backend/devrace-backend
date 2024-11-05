package com.devrace.domain.follow.repository;

import com.devrace.domain.follow.controller.dto.FollowerResponseDto;
import com.devrace.domain.follow.entity.Follower;
import com.devrace.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Follower> findByFollowerAndFollowing(User following, User follower);

    List<Follower> findByFollowing(User user);
}
