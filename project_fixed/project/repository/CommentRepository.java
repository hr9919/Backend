package com.example.project.repository;

import com.example.project.entity.Comment;
import com.example.project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewOrderByCreatedAtAsc(Review review);
}
