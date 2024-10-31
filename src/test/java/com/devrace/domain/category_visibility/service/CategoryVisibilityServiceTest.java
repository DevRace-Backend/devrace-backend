package com.devrace.domain.category_visibility.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.devrace.domain.category_visibility.entity.CategoryVisibility;
import com.devrace.domain.category_visibility.enums.CategoryType;
import com.devrace.domain.category_visibility.repository.CategoryVisibilityRepository;
import com.devrace.domain.user.entity.User;
import com.devrace.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class CategoryVisibilityServiceTest {

    @Autowired
    private CategoryVisibilityService categoryVisibilityService;

    @Autowired
    private CategoryVisibilityRepository categoryVisibilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("createCategoryVisibility(유저): 회원 가입 시 유저 정보를 받아서 초기 카테고리 공개 여부를 생성한다.")
    void createCategoryVisibility_success() {
        // given
        final User user = createUser();
        final int size = CategoryType.values().length;

        // when
        categoryVisibilityService.createCategoryVisibility(user);

        // then
        List<CategoryVisibility> result = categoryVisibilityRepository.findAll();
        assertThat(result).hasSize(size);
    }

    private User createUser() {
        return userRepository.save(new User("Hut234", "test@gmail.com", "http://imageUrl.com", true, "tester"));
    }
}