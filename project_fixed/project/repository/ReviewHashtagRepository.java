package com.example.project.repository;

import com.example.project.entity.ReviewHashtag;
import com.example.project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long> {
    List<ReviewHashtag> findByReview(Review review);
    void deleteByReview(Review review);
}
