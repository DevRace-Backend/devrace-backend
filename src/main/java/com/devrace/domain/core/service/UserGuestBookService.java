package com.devrace.domain.core.service;

import com.devrace.domain.guest_book.controller.dto.request.GuestBookCreateRequest;
import com.devrace.domain.guest_book.service.GuestBookService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGuestBookService {

    private final UserService userService;
    private final GuestBookService guestBookService;

    public void createMyPageGuestBook(Long writerId, GuestBookCreateRequest request) {
        String myPageOwnerNickname = request.getMyPageOwnerNickname();
        User myPageOwner = userService.getUserByNickname(myPageOwnerNickname);
        User writer = userService.getUserById(writerId);

        guestBookService.createGuestBook(myPageOwner, writer, request.getContent());
    }
}
