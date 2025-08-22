package com.example.project.repository;

import com.example.project.entity.Review;
import com.example.project.entity.User;
import com.example.project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUser(User user);
    
    List<Review> findByBook(Book book);

    void deleteByUser(User user);
    
    // 통계 리포트용 메서드 추가
    long countByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
}
