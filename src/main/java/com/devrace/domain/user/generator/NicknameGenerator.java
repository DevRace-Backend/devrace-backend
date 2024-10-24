package com.devrace.domain.user.generator;

import java.util.UUID;

public class NicknameGenerator {

    private NicknameGenerator() { }

    /**
     * UUID : 8-4-4-4-12
     * @return USER_XXXXX (21자)
     */
    public static String generateRandomNickname() {
        String prefix = "USER_";
        String[] uuids = UUID.randomUUID().toString().split("-");
        return prefix.concat(uuids[0]).concat(uuids[1]).concat(uuids[2]);
    }
}
