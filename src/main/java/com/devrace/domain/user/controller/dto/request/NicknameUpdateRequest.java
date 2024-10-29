package com.devrace.domain.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameUpdateRequest {

    public static final String MESSAGE = "변경할 닉네임은 1자 이상 21자 이하로 입력해 주세요.";

    @Schema(description = "변경할 닉네임", example = "새로운 닉네임임돠")
    @NotBlank(message = MESSAGE)
    @Size(min = 1, max = 21, message = MESSAGE)
    private String nickname;

    public NicknameUpdateRequest(String nickname) {
        this.nickname = nickname;
    }
}
