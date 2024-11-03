package com.devrace.domain.guest_book.entity;

import com.devrace.domain.user.entity.User;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_writer_id", columnList = "writer_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestBook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 400, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_page_owner_id", nullable = false)
    private User myPageOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    public GuestBook(String content, User myPageOwner, User writer) {
        this.content = content;
        this.myPageOwner = myPageOwner;
        this.writer = writer;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
