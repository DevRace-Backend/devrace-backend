package com.devrace.domain.guest_book.service;

import com.devrace.domain.guest_book.entity.GuestBook;
import com.devrace.domain.guest_book.repository.GuestBookRepository;
import com.devrace.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;

    public void createGuestBook(User myPageOwner, User writer, String content) {
        guestBookRepository.save(new GuestBook(content, myPageOwner, writer));
    }
}
