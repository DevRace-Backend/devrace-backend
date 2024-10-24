package com.devrace.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*  400 BAD_REQUEST : 잘못된 요청  */
    DATETIME_PARSE_ERROR(400, "만료 시간 파싱 실패"),
    NUMBER_FORMAT_ERROR(400, "Error parsing, Too long"),
    ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),
    INVALID_PROVIDER_ERROR(400, "지원하지 않는 소셜 로그인 시도"),

    /*  401 UNAUTHORIZED : 인증 안됨  */


    /*  403 FORBIDDEN : 권한 없음  */
    DENIED_AUTHORITY(403, "권한이 없습니다."),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    ACCESS_DENIED(404, "접근 권한이 없습니다."),
    NOT_FOUND_FLASH_ERROR(404, "존재하지 않는 FLASH 입니다."),
    NOT_FOUND_USER_ERROR(404, "존재하지 않는 USER 입니다."),
    NOT_FOUND_COMMENT_ERROR(404, "존재하지 않는 COMMENT 입니다."),
    NOT_FOUND_ENTITY_ERROR(404, "존재하지 않는 Entity 입니다."),

    /*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
    TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

    /*  409 CONFLICT : Resource 중복  */
    ALREADY_EXIST_USERID(409, "이미 존재하는 USERID 입니다."),

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),
    INTERRUPTED_ERROR(500, " Interrupted 에러 발생."),
    MEMCACHED_ERROR(500, "Memcached 에러 발생")
    ;

    private final Integer httpStatus;
    private final String message;
}
