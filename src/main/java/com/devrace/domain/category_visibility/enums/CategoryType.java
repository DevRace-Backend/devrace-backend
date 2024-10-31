package com.devrace.domain.category_visibility.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    ALGORITHM("알고리즘"),
    LOG("개발 일지"),
    COMMIT("커밋"),
    GUEST_BOOK("방명록"),
    STATISTIC("분석"),
    FOLLOW("팔로우")
    ;

    private final String description;
}
