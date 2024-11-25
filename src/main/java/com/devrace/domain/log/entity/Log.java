package com.devrace.domain.log.entity;

import com.devrace.domain.log.controller.dto.EditLogDto;
import com.devrace.domain.log.controller.dto.EditLogResponseDto;
import com.devrace.domain.user.entity.User;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Log extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false, length = 60)
    private String title;

    @Column(nullable = false, length = 120)
    private String content;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Log (String address, String title, String content, boolean isPublic, User user) {
        this.address = address;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.user = user;
    }

    @Transactional
    public void editLog(EditLogDto editLogDto) {
        this.address = Optional.ofNullable(editLogDto.getAddress()).orElse(this.address);
        this.title = Optional.ofNullable(editLogDto.getTitle()).orElse(this.title);
        this.content = Optional.ofNullable(editLogDto.getContent()).orElse(this.content);
        this.isPublic = Optional.ofNullable(editLogDto.getIsPublic()).orElse(this.isPublic);
    }


}
