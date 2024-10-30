package com.devrace.domain.user.controller.dto.request;

import static com.devrace.global.exception.ErrorCode.INVALID_BLOG_ADDRESS;

import com.devrace.global.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
public class BlogAddressUpdateRequest {

    public static final String MESSAGE = "블로그 주소는 https://를 포함한 최소 8글자 이상으로 입력해 주세요.";
    public static final String BLOG_ADDRESS_PREFIX = "https://";

    @Schema(description = "변경할 블로그 주소", example = "https://velog.io/@Example")
    private String blogAddress;

    public BlogAddressUpdateRequest(String blogAddress) {
        if (isNotValidBlogAddress(blogAddress)) {
            throw new CustomException(INVALID_BLOG_ADDRESS);
        }

        this.blogAddress = blogAddress;
    }

    private boolean isNotValidBlogAddress(String blogAddress) {
        return !StringUtils.hasText(blogAddress) ||
                blogAddress.length() < BLOG_ADDRESS_PREFIX.length() ||
                !blogAddress.startsWith(BLOG_ADDRESS_PREFIX);
    }
}
