package com.devrace.domain.follow.repository.following;

import java.util.List;

import com.devrace.domain.follow.entity.Follower;
import com.devrace.domain.follow.entity.Following;

public interface FollowingRepositoryCustom {

	List<Following> searchFollowing(String nickname, Long userId);
}
