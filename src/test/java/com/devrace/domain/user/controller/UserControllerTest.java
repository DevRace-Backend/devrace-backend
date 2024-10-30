package com.devrace.domain.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devrace.domain.user.controller.dto.request.NicknameUpdateRequest;
import com.devrace.domain.user.controller.dto.response.UserInfoResponse;
import com.devrace.domain.user.service.UserService;
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
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(1L, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("getUserById(유저PK): 유저 PK를 받아 사용자를 조회한다.")
    void getUserById() throws Exception {
        // given
        final String uri = "/api/v1/users/me";

        // expected
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getUserByNickname(닉네임): 닉네임을 받아 사용자를 조회한다.")
    void getUserByNickname() throws Exception {
        // given
        final String uri = "/api/v1/users";
        final String nickname = "Hut234";
        final UserInfoResponse mockedResponse = new UserInfoResponse(nickname, "테스트입니다.", "https://velog.io/@tester", "https://github.com/Hut234");

        when(userService.getUserInfo(nickname)).thenReturn(mockedResponse);

        // expected
        mockMvc.perform(get(uri).queryParam("nickname", nickname))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(mockedResponse.getNickname()))
                .andExpect(jsonPath("$.description").value(mockedResponse.getDescription()))
                .andExpect(jsonPath("$.blogAddress").value(mockedResponse.getBlogAddress()))
                .andExpect(jsonPath("$.githubAddress").value(mockedResponse.getGithubAddress()));
    }

    @Test
    @DisplayName("updateNickname(사용자PK, 변경할 닉네임): 변경할 닉네임을 받아 사용자 닉네임을 변경한다.")
    void updateNickname_success() throws Exception {
        // given
        final String uri = "/api/v1/users/me/nickname";

        final String newNickname = "NewNickname";
        final NicknameUpdateRequest request = new NicknameUpdateRequest(newNickname);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("updateNickname(사용자PK, 변경할 닉네임): 변경할 닉네임이 null 이면 실패한다.")
    void updateNickname_nickname_null_validation() throws Exception {
        // given
        final String uri = "/api/v1/users/me/nickname";

        final NicknameUpdateRequest request = new NicknameUpdateRequest(null);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(NicknameUpdateRequest.MESSAGE));
    }

    @Test
    @DisplayName("updateNickname(사용자PK, 변경할 닉네임): 변경할 닉네임이 1자 미만이면 실패한다.")
    void updateNickname_nickname_min_validation() throws Exception {
        // given
        final String uri = "/api/v1/users/me/nickname";

        final String newNickname = "";
        final NicknameUpdateRequest request = new NicknameUpdateRequest(newNickname);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(NicknameUpdateRequest.MESSAGE));
    }

    @Test
    @DisplayName("updateNickname(사용자PK, 변경할 닉네임): 변경할 닉네임이 21자 초과하면 실패한다.")
    void updateNickname_nickname_max_validation() throws Exception {
        // given
        final String uri = "/api/v1/users/me/nickname";

        final String newNickname = "0123456789012345678912";
        final NicknameUpdateRequest request = new NicknameUpdateRequest(newNickname);
        final String requestBody = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(patch(uri).with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getMessage()))
                .andExpect(jsonPath("$.data[0].message").value(NicknameUpdateRequest.MESSAGE));
    }
}