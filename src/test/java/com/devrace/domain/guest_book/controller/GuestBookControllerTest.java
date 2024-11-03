package com.devrace.domain.guest_book.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devrace.domain.core.service.UserGuestBookService;
import com.devrace.domain.guest_book.controller.dto.request.ContentUpdateRequest;
import com.devrace.domain.guest_book.controller.dto.request.GuestBookCreateRequest;
import com.devrace.domain.guest_book.service.GuestBookService;
import com.devrace.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(GuestBookController.class)
class GuestBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GuestBookService guestBookService;

    @MockBean
    private UserGuestBookService userGuestBookService;

    @BeforeEach
    void setUp() {
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("getGuestBookList(마이페이지 주인 닉네임, 페이지, 사이즈): 마이페이지 주인의 방명록을 페이지, 사이즈 정보만큼 가져온다.")
    void getGuestBookList() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";
        final String nickname = "Hut234";
        final String page = "0";
        final String size = "10";

        // expected
        mockMvc.perform(get(uri)
                        .queryParam("nickname", nickname)
                        .queryParam("page", page)
                        .queryParam("size", size))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 작성자 PK, 마이페이지 주인 닉네임, 방명록 내용을 입력받아 방명록을 생성한다.")
    void createGuestBook_success() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final GuestBookCreateRequest request = new GuestBookCreateRequest("마이페이지 주인", "방명록 내용");
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 마이페이지 주인 닉네임이 null 이면 실패한다.")
    void createGuestBook_myPageOwnerNickname_null_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final GuestBookCreateRequest request = new GuestBookCreateRequest(null, "방명록 내용");
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.NICKNAME_MESSAGE));
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 마이페이지 주인 닉네임이 1자 미만이면 실패한다.")
    void createGuestBook_myPageOwnerNickname_min_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final String myPageOwnerNickname = "";
        final GuestBookCreateRequest request = new GuestBookCreateRequest(myPageOwnerNickname, "방명록 내용");
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.NICKNAME_MESSAGE));
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 마이페이지 주인 닉네임이 공백이면 실패한다.")
    void createGuestBook_myPageOwnerNickname_min_validation2() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final String myPageOwnerNickname = "   ";
        final GuestBookCreateRequest request = new GuestBookCreateRequest(myPageOwnerNickname, "방명록 내용");
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.NICKNAME_MESSAGE));
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 마이페이지 주인 닉네임이 21자 초과하면 실패한다.")
    void createGuestBook_myPageOwnerNickname_max_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final int count = 22;
        final String myPageOwnerNickname = "A".repeat(count);
        final GuestBookCreateRequest request = new GuestBookCreateRequest(myPageOwnerNickname, "방명록 내용");
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.NICKNAME_MESSAGE));
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 방명록 내용이 null 이면 실패한다.")
    void createGuestBook_content_null_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final GuestBookCreateRequest request = new GuestBookCreateRequest("마이페이지 주인", null);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 방명록 내용이 1자 미만이면 실패한다.")
    void createGuestBook_content_min_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final String content = "";
        final GuestBookCreateRequest request = new GuestBookCreateRequest("마이페이지 주인", content);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 방명록 내용이 공백이면 실패한다.")
    void createGuestBook_content_min_validation2() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final String content = "   ";
        final GuestBookCreateRequest request = new GuestBookCreateRequest("마이페이지 주인", content);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("createGuestBook(작성자PK, 방명록 생성 DTO(마이페이지 주인 닉네임, 내용)): 방명록 내용이 400자 초과하면 실패한다.")
    void createGuestBook_content_max_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books";

        final int count = 401;
        final String content = "A".repeat(count);
        final GuestBookCreateRequest request = new GuestBookCreateRequest("마이페이지 주인", content);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(GuestBookCreateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("updateContent(작성자PK, 방명록PK, 방명록 수정 내용): 방명록 수정 내용을 입력받아 방명록 내용을 수정한다.")
    void updateContent_success() throws Exception {
        // given
        final String uri = "/api/v1/guest-books/{guestBookId}";

        final ContentUpdateRequest request = new ContentUpdateRequest("수정할 방명록 내용");
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri, 1L).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("updateContent(작성자PK, 방명록PK, 방명록 수정 내용): 방명록 수정 내용이 null 이면 실패한다.")
    void updateContent_content_null_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books/{guestBookId}";

        final ContentUpdateRequest request = new ContentUpdateRequest(null);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri, 1L).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(ContentUpdateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("updateContent(작성자PK, 방명록PK, 방명록 수정 내용): 방명록 수정 내용이 1자 미만이면 실패한다.")
    void updateContent_content_min_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books/{guestBookId}";

        final String newContent = "";
        final ContentUpdateRequest request = new ContentUpdateRequest(newContent);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri, 1L).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(ContentUpdateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("updateContent(작성자PK, 방명록PK, 방명록 수정 내용): 방명록 수정 내용이 공백이면 실패한다.")
    void updateContent_content_min_validation2() throws Exception {
        // given
        final String uri = "/api/v1/guest-books/{guestBookId}";

        final String newContent = "   ";
        final ContentUpdateRequest request = new ContentUpdateRequest(newContent);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri, 1L).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(ContentUpdateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("updateContent(작성자PK, 방명록PK, 방명록 수정 내용): 방명록 수정 내용이 400자 초과하면 실패한다.")
    void updateContent_content_max_validation() throws Exception {
        // given
        final String uri = "/api/v1/guest-books/{guestBookId}";

        final String newContent = "A".repeat(401);
        final ContentUpdateRequest request = new ContentUpdateRequest(newContent);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri, 1L).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(ContentUpdateRequest.CONTENT_MESSAGE));
    }

    @Test
    @DisplayName("deleteGuestBook(작성자PK, 방명록PK): 작성자PK, 방명록PK를 입력받아 방명록을 삭제한다.")
    void deleteGuestBook() throws Exception {
        // given
        final String uri = "/api/v1/guest-books/{guestBookId}";

        // expected
        mockMvc.perform(delete(uri, 1L).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}