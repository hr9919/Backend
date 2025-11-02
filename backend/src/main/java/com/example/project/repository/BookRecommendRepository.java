package com.example.project.repository;

import com.example.project.entity.BookRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRecommendRepository extends JpaRepository<BookRecommend, Long> {
    Page<BookRecommend> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}