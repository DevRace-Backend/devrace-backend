package com.devrace.domain.user.repository;

import com.devrace.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPrimaryEmail(String primaryEmail);
    Optional<User> findByNickname(String nickname);
    boolean existsByNickname(String nickname);

}
