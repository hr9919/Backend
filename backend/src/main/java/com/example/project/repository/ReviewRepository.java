package com.example.project.repository;

import com.example.project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser_Id(Long userId);
    List<Review> findByBook_Id(Long bookId);
}