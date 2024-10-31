package com.devrace.domain.level.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevelName userLevelName;

    @Column(nullable = false)
    private String levelBadgeImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1DNsfskjJtWhewcdCt8_4spNsZf7lmL3wKQ&s";
}
