package com.devrace.domain.category_visibility.entity;

import com.devrace.domain.category_visibility.enums.CategoryType;
import com.devrace.domain.user.entity.User;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_user_type", columnList = "user_id, type"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryVisibility extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @Column(nullable = false)
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public static List<CategoryVisibility> initializeVisibilities(User user) {
        return Arrays.stream(CategoryType.values())
                .map(type -> new CategoryVisibility(type, user))
                .toList();
    }

    public void changeVisibility(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public CategoryVisibility(CategoryType type, User user) {
        this.type = type;
        this.isPublic = true;
        this.user = user;
    }
}
