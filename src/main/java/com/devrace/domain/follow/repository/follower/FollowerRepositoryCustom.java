package com.devrace.domain.follow.repository.follower;

import java.util.List;

import com.devrace.domain.follow.controller.dto.FollowerResponseDto;
import com.devrace.domain.follow.entity.Follower;

public interface FollowerRepositoryCustom {
	List<Follower> searchFollower(String nickname, Long userId);
}
