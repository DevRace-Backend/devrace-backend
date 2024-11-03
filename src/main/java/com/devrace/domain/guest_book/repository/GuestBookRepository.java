package com.devrace.domain.guest_book.repository;

import com.devrace.domain.guest_book.entity.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

}
