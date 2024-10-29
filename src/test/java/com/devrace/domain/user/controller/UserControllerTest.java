package com.devrace.domain.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devrace.domain.user.controller.dto.UserInfoResponse;
import com.devrace.domain.user.service.UserService;
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
        final String url = "/api/v1/users/me";

        // expected
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getUserByNickname(닉네임): 닉네임을 받아 사용자를 조회한다.")
    void getUserByNickname() throws Exception {
        // given
        final String url = "/api/v1/users";
        final String nickname = "Hut234";
        final UserInfoResponse mockedResponse = new UserInfoResponse(nickname, "테스트입니다.", "https://velog.io/@tester", "https://github.com/Hut234");

        when(userService.getUserInfo(nickname)).thenReturn(mockedResponse);

        // expected
        mockMvc.perform(get(url).queryParam("nickname", nickname))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(mockedResponse.getNickname()))
                .andExpect(jsonPath("$.description").value(mockedResponse.getDescription()))
                .andExpect(jsonPath("$.blogAddress").value(mockedResponse.getBlogAddress()))
                .andExpect(jsonPath("$.githubAddress").value(mockedResponse.getGithubAddress()));
    }
}