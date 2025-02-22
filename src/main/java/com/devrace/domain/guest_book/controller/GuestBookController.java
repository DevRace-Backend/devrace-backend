package com.devrace.domain.guest_book.controller;

import com.devrace.domain.core.service.UserGuestBookService;
import com.devrace.domain.guest_book.controller.dto.request.ContentUpdateRequest;
import com.devrace.domain.guest_book.controller.dto.request.GuestBookCreateRequest;
import com.devrace.domain.guest_book.controller.dto.response.GuestBookInfoResponse;
import com.devrace.domain.guest_book.service.GuestBookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guest-books")
public class GuestBookController {

    private final UserGuestBookService userGuestBookService;
    private final GuestBookService guestBookService;

    @GetMapping
    public ResponseEntity<List<GuestBookInfoResponse>> getGuestBookList(@RequestParam String nickname, @RequestParam int page, @RequestParam int size) {
        List<GuestBookInfoResponse> response = userGuestBookService.getMyPageGuestBookList(nickname, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createGuestBook(@AuthenticationPrincipal Long userId, @Valid @RequestBody GuestBookCreateRequest request) {
        userGuestBookService.createMyPageGuestBook(userId, request);
        return ResponseEntity.created(null).build();
    }

    @PatchMapping("{guestBookId}")
    public ResponseEntity<Void> updateContent(@AuthenticationPrincipal Long userId, @PathVariable Long guestBookId, @Valid @RequestBody ContentUpdateRequest request) {
        guestBookService.updateContent(userId, guestBookId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{guestBookId}")
    public ResponseEntity<Void> deleteGuestBook(@AuthenticationPrincipal Long userId, @PathVariable Long guestBookId) {
        guestBookService.deleteGuestBook(userId, guestBookId);
        return ResponseEntity.noContent().build();
    }
}
