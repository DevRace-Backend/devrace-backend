package com.devrace.domain.guest_book.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.devrace.domain.guest_book.entity.GuestBook;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class GuestBookRepositoryTest {

    @Autowired
    private GuestBookRepository guestBookRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("findAllByMyPageOwnerId(마이페이지 주인 PK): 마이페이지 주인으로 방명록 리스트를 조회한다")
    void findAllByMyPageOwner() {
        // given
        final User owner = createUser("writer", "writer@gmail.com", "writer");
        final User writer = createUser("owner", "owner@gmail.com", "owner");
        final PageRequest request = PageRequest.of(0, 10);

        final int size = 5;
        for (int i = 0; i < size; i++) {
            createGuestBook("content", owner, writer);
        }

        // when
        List<GuestBook> result = guestBookRepository.findAllByMyPageOwnerId(owner.getId(), request);

        // then
        assertThat(result).hasSize(size);
    }

    private GuestBook createGuestBook(String content, User owner, User writer) {
        return guestBookRepository.save(new GuestBook(content, owner, writer));
    }

    private User createUser(String nickname, String email, String githubName) {
        return userRepository.save(new User(nickname, email, "http://imageUrl.com", true, githubName));
    }

}