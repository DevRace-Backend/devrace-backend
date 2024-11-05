package com.devrace.domain.algorithm.comment.service;

import com.devrace.domain.algorithm.comment.controller.dto.CommentResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CommentServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("댓글 잘 가져오는지 확인")
    void getCommentList() throws Exception {
        Long solutionId = 1L;

        List<CommentResponseDto> mockComments = List.of(
                CommentResponseDto.builder()
                        .id(1L)
                        .nickName("testUser")
                        .profileImage("https://example.com/image1.jpg")
                        .content("테스트 댓글")
                        .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                        .build(),
                CommentResponseDto.builder()
                        .id(2L)
                        .nickName("testUser2")
                        .profileImage("https://example.com/image2.jpg")
                        .content("테스트 댓글2")
                        .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                        .build()
        );

        when(commentService.getCommentList(solutionId, 0, 10)).thenReturn(mockComments);

        mockMvc.perform(get("/api/v1/algorithm/comment/{solutionId}", solutionId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nickName").value("testUser"))
                .andExpect(jsonPath("$[1].nickName").value("testUser2"));

    }

    @Test
    @DisplayName("페이지네이션이 잘 되는지 확인")
    public void getgetCommentListWithPage() throws Exception {
        Long solutionId = 1L;

        List<CommentResponseDto> mockCommentsPage1 = List.of(
                CommentResponseDto.builder()
                        .id(1L)
                        .nickName("testUser")
                        .profileImage("https://example.com/image1.jpg")
                        .content("테스트 댓글")
                        .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                        .build(),
                CommentResponseDto.builder()
                        .id(2L)
                        .nickName("testUser2")
                        .profileImage("https://example.com/image2.jpg")
                        .content("테스트 댓글2")
                        .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                        .build()
        );

        List<CommentResponseDto> mockCommentsPage2 = List.of(
                CommentResponseDto.builder()
                        .id(3L)
                        .nickName("testUser3")
                        .profileImage("https://example.com/image3.jpg")
                        .content("테스트 댓글3")
                        .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                        .build(),
                CommentResponseDto.builder()
                        .id(4L)
                        .nickName("testUser4")
                        .profileImage("https://example.com/image4.jpg")
                        .content("테스트 댓글4")
                        .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                        .build()
        );

        when(commentService.getCommentList(solutionId, 0, 2)).thenReturn(mockCommentsPage1);
        mockMvc.perform(get("/api/v1/algorithm/comment/{solutionId}", solutionId)
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nickName").value("testUser"))
                .andExpect(jsonPath("$[1].nickName").value("testUser2"));

        when(commentService.getCommentList(solutionId, 1, 2)).thenReturn(mockCommentsPage2);
        mockMvc.perform(get("/api/v1/algorithm/comment/{solutionId}", solutionId)
                        .param("page", "1")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nickName").value("testUser3"))
                .andExpect(jsonPath("$[1].nickName").value("testUser4"));
    }

    @Test
    @DisplayName("페이지 경계값 테스트")
    public void borderLisneTest() throws Exception {
        Long solutionId = 1L;

        List<CommentResponseDto> mockComments = List.of(
                CommentResponseDto.builder()
                        .id(1L)
                        .nickName("testUser3")
                        .profileImage("https://example.com/image1.jpg")
                        .content("This is a test comment")
                        .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                        .build()
        );

        // Mocking 설정: commentService.getCommentList()가 호출될 때 mockComments 반환
        when(commentService.getCommentList(solutionId, 0, 10)).thenReturn(mockComments);

        // 테스트 실행 및 검증
        mockMvc.perform(get("/api/v1/algorithm/comment/{solutionId}", solutionId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // 리스트 크기가 1인지 확인
                .andExpect(jsonPath("$[0].nickName").value("testUser3")); // nickName이 예상한 값인지 확인
    }

}