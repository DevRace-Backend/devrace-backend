package com.devrace.domain.category_visibility.service;

import com.devrace.domain.category_visibility.entity.CategoryVisibility;
import com.devrace.domain.category_visibility.repository.CategoryVisibilityRepository;
import com.devrace.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryVisibilityService {

    private final CategoryVisibilityRepository categoryVisibilityRepository;

    @Transactional
    public void createCategoryVisibility(User user) {
        List<CategoryVisibility> categoryVisibilities = CategoryVisibility.initializeVisibilities(user);
        categoryVisibilityRepository.saveAll(categoryVisibilities);
    }
}
