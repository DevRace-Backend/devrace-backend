package com.devrace.domain.guest_book.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuestBookCreateRequest {

    public static final String NICKNAME_MESSAGE = "마이페이지 주인 닉네임은 1자 이상 21자 이하로 입력해 주세요.";
    public static final String CONTENT_MESSAGE = "방명록은 1자 이상 400자 이하로 입력해 주세요.";

    @Schema(description = "마이페이지 주인 닉네임", example = "USER_A303")
    @NotBlank(message = NICKNAME_MESSAGE)
    @Size(max = 21, message = NICKNAME_MESSAGE)
    private String myPageOwnerNickname;

    @Schema(description = "방명록 내용", example = "방명록 남기고 갑니다.")
    @NotBlank(message = CONTENT_MESSAGE)
    @Size(max = 400, message = CONTENT_MESSAGE)
    private String content;

    public GuestBookCreateRequest(String myPageOwnerNickname, String content) {
        this.myPageOwnerNickname = myPageOwnerNickname;
        this.content = content;
    }
}
