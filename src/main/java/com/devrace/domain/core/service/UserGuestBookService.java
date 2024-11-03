package com.devrace.domain.core.service;

import com.devrace.domain.guest_book.controller.dto.request.GuestBookCreateRequest;
import com.devrace.domain.guest_book.controller.dto.response.GuestBookInfoResponse;
import com.devrace.domain.guest_book.entity.GuestBook;
import com.devrace.domain.guest_book.service.GuestBookService;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

        guestBookService.createGuestBook(request.getContent(), myPageOwner, writer);
    }

    public List<GuestBookInfoResponse> getMyPageGuestBookList(String nickname, int page, int size) {
        User myPageOwner = userService.getUserByNickname(nickname);

        // todo: 상신 count entity 정의 시 userLevel 받아오기

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
        List<GuestBook> guestBooks = guestBookService.getAllByMyPageOwnerId(myPageOwner.getId(), pageRequest);

        return guestBooks.stream()
                .map(guestBook -> GuestBookInfoResponse.from(guestBook, null))
                .toList();
    }
}
