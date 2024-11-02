package com.devrace.domain.guest_book.service;

import static com.devrace.global.exception.ErrorCode.FORBIDDEN;
import static com.devrace.global.exception.ErrorCode.GUEST_BOOK_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devrace.domain.guest_book.controller.dto.request.ContentUpdateRequest;
import com.devrace.domain.guest_book.entity.GuestBook;
import com.devrace.domain.guest_book.repository.GuestBookRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import com.devrace.global.exception.CustomException;
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
    void createGuestBook_success() {
        // given
        final User owner = createUser("myPageOwner", "owner@gmail.com", "owner");
        final User writer = createUser("writer", "writer@gmail.com", "writer");
        final String content = "방명록입니다.";
        final int size = 10;

        // when
        for (int i = 0; i < size; i++) {
            guestBookService.createGuestBook(content, owner, writer);
        }

        List<GuestBook> result = guestBookRepository.findAll();

        // then
        assertThat(result).hasSize(size);
    }

    @Test
    @DisplayName("updateContent(작성자ID, 방명록ID, 방명록 수정 내용): 변경할 내용을 입력받아 방명록 내용을 변경한다.")
    void updateContent_success() {
        // given
        final User owner = createUser("myPageOwner", "owner@gmail.com", "owner");
        final User writer = createUser("writer", "writer@gmail.com", "writer");
        final String originContent = "방명록 작성";
        final GuestBook savedGuestBook = guestBookRepository.save(new GuestBook(originContent, owner, writer));

        final Long guestBookId = savedGuestBook.getId();
        final String newContent = "변경한 방명록 작성";
        final ContentUpdateRequest request = new ContentUpdateRequest(newContent);

        // when
        guestBookService.updateContent(writer.getId(), guestBookId, request);

        // then
        assertThat(request.getContent()).isEqualTo(savedGuestBook.getContent());
    }

    @Test
    @DisplayName("updateContent(작성자ID, 방명록ID, 방명록 수정 내용): 방명록의 작성자와 수정을 시도하는 유저의 PK가 다르면 실패한다.")
    void updateContent_is_not_a_writer_fail() {
        // given
        final User owner = createUser("myPageOwner", "owner@gmail.com", "owner");
        final User writer = createUser("writer", "writer@gmail.com", "writer");
        final String originContent = "방명록 작성";
        final GuestBook savedGuestBook = guestBookRepository.save(new GuestBook(originContent, owner, writer));

        final Long wrongWriterId = owner.getId();
        final Long guestBookId = savedGuestBook.getId();
        final String newContent = "변경한 방명록 작성";
        final ContentUpdateRequest request = new ContentUpdateRequest(newContent);

        // expected
        assertThatThrownBy(() -> guestBookService.updateContent(wrongWriterId, guestBookId, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(FORBIDDEN.getMessage());
    }

    @Test
    @DisplayName("getGuestBookById(작성자ID, 방명록ID, 방명록 수정 내용): 존재하지 않는 방명록 PK로 방명록 조회 시 실패한다.")
    void getGuestBookById_does_not_exist_id_fail() {
        // given
        final User owner = createUser("myPageOwner", "owner@gmail.com", "owner");
        final User writer = createUser("writer", "writer@gmail.com", "writer");
        final String originContent = "방명록 작성";
        guestBookRepository.save(new GuestBook(originContent, owner, writer));

        final Long wrongGuestBookId = Long.valueOf(guestBookRepository.count() + 1L);
        final String newContent = "변경한 방명록 작성";
        final ContentUpdateRequest request = new ContentUpdateRequest(newContent);

        // expected
        assertThatThrownBy(() -> guestBookService.updateContent(writer.getId(), wrongGuestBookId, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(GUEST_BOOK_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("deleteGuestBook(작성자ID, 방명록ID): 입력한 방명록의 작성자 ID와 입력받은 작성자 ID가 일치하면 방명록을 삭제한다.")
    void deleteGuestBook_success() {
        // given
        final String content = "방명록 작성";
        final User owner = createUser("myPageOwner", "owner@gmail.com", "owner");
        final User writer = createUser("writer", "writer@gmail.com", "writer");

        final GuestBook guestBook1 = createGuestBook(content, owner, writer);
        createGuestBook(content, owner, writer);
        createGuestBook(content, owner, writer);

        // when
        guestBookService.deleteGuestBook(writer.getId(), guestBook1.getId());

        // then
        long guestBooks = guestBookRepository.count();
        assertThat(guestBooks).isEqualTo(2L);
    }

    private GuestBook createGuestBook(String content, User owner, User writer) {
        return guestBookRepository.save(new GuestBook(content, owner, writer));
    }

    private User createUser(String nickname, String email, String githubName) {
        return userRepository.save(new User(nickname, email, "http://imageUrl.com", true, githubName));
    }
}