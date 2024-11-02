package com.devrace.domain.guest_book.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.devrace.domain.guest_book.entity.GuestBook;
import com.devrace.domain.guest_book.repository.GuestBookRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class GuestBookServiceTest {

    @Autowired
    private GuestBookService guestBookService;

    @Autowired
    private GuestBookRepository guestBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("createGuestBook(마이페이지주인, 작성자, 내용): 입력받은 정보를 바탕으로 방명록을 생성한다.")
    void createGuestBook() {
        // given
        final User owner = createUser("myPageOwner", "owner@gmail.com", "owner");
        final User writer = createUser("writer", "writer@gmail.com", "writer");
        final String content = "방명록입니다.";
        final int size = 10;

        userRepository.saveAll(List.of(owner, writer));

        // when
        for (int i = 0; i < size; i++) {
            guestBookService.createGuestBook(owner, writer, content);
        }

        List<GuestBook> result = guestBookRepository.findAll();

        // then
        assertThat(result).hasSize(size);
    }

    private User createUser(String nickname, String email, String githubName) {
        return new User(nickname, email, "http://imageUrl.com", true, githubName);
    }
}