package com.devrace.domain.guest_book.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContentUpdateRequest {

    public static final String GUEST_BOOK_ID_MESSAGE = "방명록 ID를 반드시 입력해 주세요.";
    public static final String CONTENT_MESSAGE = "방명록은 1자 이상 400자 이하로 입력해 주세요.";

    @Schema(description = "방명록 PK", example = "1")
    @NotNull(message = GUEST_BOOK_ID_MESSAGE)
    private Long guestBookId;

    @Schema(description = "방명록 내용", example = "방명록 남기고 갑니다.")
    @NotBlank(message = CONTENT_MESSAGE)
    @Size(max = 400, message = CONTENT_MESSAGE)
    private String content;

    public ContentUpdateRequest(Long guestBookId, String content) {
        this.guestBookId = guestBookId;
        this.content = content;
    }
}
