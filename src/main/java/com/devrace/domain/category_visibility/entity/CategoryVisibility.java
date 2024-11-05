package com.devrace.domain.category_visibility.entity;

import com.devrace.domain.category_visibility.enums.CategoryType;
import com.devrace.domain.user.entity.User;
import com.devrace.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_user_type", columnList = "user_id, type"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    private CategoryVisibility(CategoryType type, User user) {
        this.type = type;
        this.isPublic = true;
        this.user = user;
    }
}
