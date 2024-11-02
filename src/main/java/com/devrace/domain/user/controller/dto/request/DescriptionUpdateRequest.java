package com.devrace.domain.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DescriptionUpdateRequest {

    public static final String MESSAGE = "자기소개는 1자 이상 200자 이하로 입력해 주세요.";

    @Schema(description = "변경할 자기소개", example = "반갑습니다! 하하하~")
    @NotBlank(message = MESSAGE)
    @Size(max = 200, message = MESSAGE)
    private String description;

    public DescriptionUpdateRequest(String description) {
        this.description = description;
    }
}
