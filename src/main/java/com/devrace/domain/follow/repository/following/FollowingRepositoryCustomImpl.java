package com.devrace.domain.follow.repository.following;

import static com.devrace.domain.follow.entity.QFollowing.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.devrace.domain.follow.entity.Following;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FollowingRepositoryCustomImpl implements FollowingRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Following> searchFollowing(String nickname, Long userId) {
		return queryFactory.selectFrom(following1)
			.join(following1.following).fetchJoin()
			.where(
				nicknameContains(nickname),
				followerUser(userId)
			)
			.orderBy(following1.createdAt.desc())
			.fetch();
	}

	private BooleanExpression nicknameContains(String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return null;
		}
		return following1.following.nickname.containsIgnoreCase(nickname);
	}

	private BooleanExpression followerUser(Long userId) {
		return following1.follower.id.eq(userId);
	}
}
