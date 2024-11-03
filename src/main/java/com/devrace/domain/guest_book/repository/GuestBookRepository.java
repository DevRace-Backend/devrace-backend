package com.devrace.domain.guest_book.repository;

import com.devrace.domain.guest_book.entity.GuestBook;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    @Query("SELECT gb FROM GuestBook gb JOIN FETCH gb.writer WHERE gb.myPageOwner.id = :myPageOwnerId")
    List<GuestBook> findAllByMyPageOwnerId(Long myPageOwnerId, Pageable pageable);
}
