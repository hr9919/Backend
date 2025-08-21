package com.example.project.repository;

import com.example.project.entity.Like;
import com.example.project.entity.User;
import com.example.project.entity.Review;
import com.example.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndReview(User user, Review review);
    boolean existsByUserAndComment(User user, Comment comment);
    Optional<Like> findByUserAndReview(User user, Review review);
    Optional<Like> findByUserAndComment(User user, Comment comment);
    void deleteByUserAndReview(User user, Review review);
    void deleteByUserAndComment(User user, Comment comment);
}
