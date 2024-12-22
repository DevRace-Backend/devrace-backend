package com.devrace.domain.follow.repository.follower;

import static com.devrace.domain.follow.entity.QFollower.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.devrace.domain.follow.controller.dto.FollowerResponseDto;
import com.devrace.domain.follow.entity.Follower;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowerRepositoryCustomImpl implements FollowerRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Follower> searchFollower(String nickname, Long userId) {
		return queryFactory.selectFrom(follower1)
			.join(follower1.follower).fetchJoin()
			.where(
				nicknameContains(nickname),
				followingUser(userId)
			)
			.orderBy(follower1.createAt.desc())
			.fetch();
	}

	private BooleanExpression nicknameContains(String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return null;
		}
		return follower1.follower.nickname.containsIgnoreCase(nickname);
	}

	private BooleanExpression followingUser(Long userId) {
		return follower1.follower.id.eq(userId);
	}
}
