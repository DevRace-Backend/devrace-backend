package com.devrace.domain.level.entity;

import lombok.Getter;

@Getter
public enum UserLevelName {

    BABY("아기"),
    WALKING("걷기"),
    BICYCLE("자전거"),
    SPORTCAR("스포츠카"),
    AIRPLANE("비행기"),
    ROCKET("로켓");

    private final String currentUserLevelName;

    UserLevelName(String currentUserLevelName) {
        this.currentUserLevelName = currentUserLevelName;
    }
}
