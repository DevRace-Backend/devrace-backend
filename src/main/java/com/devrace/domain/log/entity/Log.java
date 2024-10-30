package com.devrace.domain.log.entity;

import com.devrace.domain.user.entity.User;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    public Log (String address, String title, String content, boolean isPublic, Long userId) {
        this.address = address;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.userId = userId;
    }

}
