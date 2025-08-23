package com.example.project.repository;

import com.example.project.entity.Review;
import com.example.project.entity.User;
import com.example.project.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUser(User user);
    
    List<Review> findByBook(Book book);

    void deleteByUser(User user);
    
    long countByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
    
    List<Review> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
