package com.devrace.domain.guest_book.controller.dto.response;

import com.devrace.domain.guest_book.entity.GuestBook;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class GuestBookInfoResponse {

    private Long guestBookId;
    private String guestBookContent;
    private ZonedDateTime guestBookCreatedAt;
    private String writerNickname;
    private String writerImageUrl;
    private String writerLevelImageUrl;

    public static GuestBookInfoResponse from(GuestBook guestBook, String writerLevelImageUrl) {
        return new GuestBookInfoResponse(
                guestBook.getId(),
                guestBook.getContent(),
                guestBook.getCreatedAt(),
                guestBook.getWriter().getNickname(),
                guestBook.getWriter().getImageUrl(),
                writerLevelImageUrl);
    }

    private GuestBookInfoResponse(Long guestBookId, String guestBookContent, ZonedDateTime guestBookCreatedAt, String writerNickname, String writerImageUrl, String writerLevelImageUrl) {
        this.guestBookId = guestBookId;
        this.guestBookContent = guestBookContent;
        this.guestBookCreatedAt = guestBookCreatedAt;
        this.writerNickname = writerNickname;
        this.writerImageUrl = writerImageUrl;
        this.writerLevelImageUrl = writerLevelImageUrl;
    }
}
