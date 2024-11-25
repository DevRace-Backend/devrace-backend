package com.devrace.domain.category_visibility.repository;

import com.devrace.domain.category_visibility.entity.CategoryVisibility;
import com.devrace.domain.category_visibility.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryVisibilityRepository extends JpaRepository<CategoryVisibility, Long> {

    Optional<CategoryVisibility> findByIdAndType(Long userId, CategoryType categoryType);

    Optional<CategoryVisibility> findByUserIdAndType(Long userId, CategoryType categoryType);
}
