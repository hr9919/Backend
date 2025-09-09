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

    // 특정 사용자가 작성한 모든 리뷰
    List<Review> findByUser(User user);

    // 특정 책의 모든 리뷰
    List<Review> findByBook(Book book);

    // 특정 사용자의 모든 리뷰 삭제
    void deleteByUser(User user);

    // 특정 기간 동안 특정 사용자가 작성한 리뷰 수
    long countByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    // 특정 사용자의 최신 리뷰를 페이징해서 가져오기
    List<Review> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // 여러 사용자(팔로잉 리스트 + 본인)의 최신 리뷰를 최신순으로 가져오기 (인스타 피드용)
    List<Review> findByUser_IdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
}
