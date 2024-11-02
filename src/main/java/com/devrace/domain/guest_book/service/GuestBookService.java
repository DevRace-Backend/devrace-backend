package com.devrace.domain.guest_book.service;

import static com.devrace.global.exception.ErrorCode.FORBIDDEN;
import static com.devrace.global.exception.ErrorCode.GUEST_BOOK_NOT_FOUND;

import com.devrace.domain.guest_book.controller.dto.request.ContentUpdateRequest;
import com.devrace.domain.guest_book.entity.GuestBook;
import com.devrace.domain.guest_book.repository.GuestBookRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.global.exception.CustomException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;

    @Transactional
    public void createGuestBook(String content, User myPageOwner, User writer) {
        guestBookRepository.save(new GuestBook(content, myPageOwner, writer));
    }

    @Transactional
    public void updateContent(Long writerId, ContentUpdateRequest request) {
        GuestBook guestBook = getGuestBookById(request.getGuestBookId());

        if (isNotWriter(writerId, guestBook)) {
            throw new CustomException(FORBIDDEN);
        }

        guestBook.changeContent(request.getContent());
    }

    private boolean isNotWriter(Long writerId, GuestBook guestBook) {
        return !Objects.equals(guestBook.getWriter().getId(), writerId);
    }

    private GuestBook getGuestBookById(Long guestBookId) {
        return guestBookRepository.findById(guestBookId)
                .orElseThrow(() -> new CustomException(GUEST_BOOK_NOT_FOUND));
    }
}
