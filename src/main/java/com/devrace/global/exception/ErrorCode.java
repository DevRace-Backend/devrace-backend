package com.devrace.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*  400 BAD_REQUEST : 잘못된 요청  */
    DUPLICATED_LINK(400, "중복된 링크입니다."),
    INVALID_LINK(400, "잘못된 링크입니다."),
    NUMBER_FORMAT_ERROR(400, "Error parsing, Too long"),
    ILLEGAL_ARGUMENT_ERROR(400, "잘못된 파라미터 전달"),
    INVALID_PROVIDER_ERROR(400, "지원하지 않는 소셜 로그인 시도"),
    EXPIRED_TOKEN(400, "만료된 토큰입니다."),
    INVALID_BLOG_ADDRESS(400,  "블로그 주소는 https://를 포함한 최소 8글자 이상으로 입력해 주세요."),

    /*  401 UNAUTHORIZED : 인증 안됨  */
    UNAUTHORIZED(401, "인증되지 않았습니다."),

    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN(403, "권한이 없습니다."),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    ACCESS_DENIED(404, "접근 권한이 없습니다."),
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    LOG_NOT_FOUND(404, "로그를 찾을 수 없습니다."),
    GITHUB_NOT_FOUND(404, "연결된 깃허브를 찾을 수 없습니다."),
    SOLUTION_NOT_FOUND(404, "제출된 알고리즘 풀이를 찾을 수 없습니다."),
    PROBLEM_NOT_FOUND(404, "알고리즘 문제를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다."),
    COMMIT_COUNT_NOT_FOUND(404, "커밋 갯수가 없습니다."),
    GUEST_BOOK_NOT_FOUND(404, "방명록을 찾을 수 없습니다."),

    /*  408 REQUEST_TIMEOUT : 요청에 대한 응답 시간 초과  */
    TIMEOUT_ERROR(408, "응답시간을 초과하였습니다."),

    /*  409 CONFLICT : Resource 중복  */
    ALREADY_EXIST_USERID(409, "이미 존재하는 USERID 입니다."),
    ALREADY_EXIST_NICKNAME(409, "이미 존재하는 NICKNAME 입니다."),

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(500, "내부 서버 에러입니다."),
    INTERRUPTED_ERROR(500, " Interrupted 에러 발생."),
    GITHUB_API_IS_NOT_VALID(500, "유효하지 않은 깃허브 API 입니다."),
    CAN_NOT_EXTRACTION_COMMIT_COUNT(500, "커밋 갯수를 가져오지 못하였습니다."),

    /*  502 BAD_GATEWAY  연결 실패   */
    FAIL_TO_CONNECT_GITHUB(502, "깃허브 API 연결에 실패하였습니다.")
    ;

    private final Integer httpStatus;
    private final String message;
}
