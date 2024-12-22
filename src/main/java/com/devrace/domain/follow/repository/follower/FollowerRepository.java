package com.devrace.domain.follow.repository.follower;

import com.devrace.domain.follow.controller.dto.FollowerResponseDto;
import com.devrace.domain.follow.entity.Follower;
import com.devrace.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long>, FollowerRepositoryCustom {
    Optional<Follower> findByFollowerAndFollowing(User following, User follower);

    @Query("SELECT f FROM Follower f JOIN fetch f.follower WHERE f.following =:user")
    List<Follower> findByFollowing(User user);
}
