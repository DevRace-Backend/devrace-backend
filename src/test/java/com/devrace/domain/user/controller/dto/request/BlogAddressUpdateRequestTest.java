package com.devrace.domain.user.controller.dto.request;

import static com.devrace.global.exception.ErrorCode.INVALID_BLOG_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devrace.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BlogAddressUpdateRequestTest {

    @Test
    @DisplayName("new BlogAddressUpdateRequest(블로그 주소): 블로그 주소를 입력받아 BlogAddressUpdateRequest 를 생성한다.")
    void newBlogAddressUpdateRequest_success() {
        // given
        final String blogAddress = "https://NewBlogAddress";

        // when
        BlogAddressUpdateRequest request = new BlogAddressUpdateRequest(blogAddress);

        // then
        assertThat(request.getBlogAddress()).isEqualTo(blogAddress);
    }

    @Test
    @DisplayName("new BlogAddressUpdateRequest(블로그 주소): 블로그 주소가 null 이면 실패한다.")
    void newBlogAddressUpdateRequest_blog_address_null_validation() {
        // given
        final String blogAddress = null;

        // expected
        assertThatThrownBy(() -> new BlogAddressUpdateRequest(blogAddress))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_BLOG_ADDRESS.getMessage());
    }

    @Test
    @DisplayName("new BlogAddressUpdateRequest(블로그 주소): 블로그 주소가 BLOG_ADDRESS_PREFIX.length() 보다 작으면 실패한다.")
    void newBlogAddressUpdateRequest_blog_address_min_validation() {
        // given
        final String blogAddress = "https:/";

        // expected
        assertThatThrownBy(() -> new BlogAddressUpdateRequest(blogAddress))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_BLOG_ADDRESS.getMessage());
    }

    @Test
    @DisplayName("new BlogAddressUpdateRequest(블로그 주소): 블로그 주소가 BLOG_ADDRESS_PREFIX 으로 시작하지 않으면 실패한다.")
    void newBlogAddressUpdateRequest_blog_address_prefix_validation() {
        // given
        final String blogAddress = "sptth://";

        // expected
        assertThatThrownBy(() -> new BlogAddressUpdateRequest(blogAddress))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_BLOG_ADDRESS.getMessage());
    }
}