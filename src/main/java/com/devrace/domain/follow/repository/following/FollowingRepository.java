package com.devrace.domain.follow.repository.following;

import com.devrace.domain.follow.entity.Following;
import com.devrace.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {
    Optional<Following> findByFollowerAndFollowing(User follower, User following);

    @Query("SELECT f FROM Following f JOIN fetch f.following WHERE f.follower =:user")
    List<Following> findByFollower(User user);
}
