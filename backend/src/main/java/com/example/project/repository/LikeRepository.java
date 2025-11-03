package com.example.project.repository;

import com.example.project.entity.Like;
import com.example.project.entity.User;
import com.example.project.entity.Review;
import com.example.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndReview(User user, Review review);
    boolean existsByUserAndComment(User user, Comment comment);

    Optional<Like> findByUserAndReview(User user, Review review);
    Optional<Like> findByUserAndComment(User user, Comment comment);

    void deleteByUserAndReview(User user, Review review);
    void deleteByUserAndComment(User user, Comment comment);

    long countByReview(Review review);
    long countByComment(Comment comment);

    boolean existsByUser_IdAndReview_Id(Long userId, Long reviewId);
    boolean existsByUser_IdAndComment_CommentId(Long userId, Long commentId);

    // 단건용
    List<Like> findByReview(Review review);
    List<Like> findByComment(Comment comment);

    // ✅ 배치: viewer가 좋아요한 리뷰 ID 목록
    @Query("""
        select l.review.id
        from Like l
        where l.user.id = :viewerId
          and l.review.id in :reviewIds
    """)
    List<Long> findMyLikedReviewIds(
            @Param("viewerId") Long viewerId,
            @Param("reviewIds") Collection<Long> reviewIds
    );

    // ✅ 배치: 여러 리뷰 좋아요를 유저와 함께 페치(최신순) → 서비스에서 리뷰별 그룹핑
    @Query("""
        select l
        from Like l
        join fetch l.user u
        where l.review.id in :reviewIds
        order by l.likeId desc
    """)
    List<Like> findByReviewIdsWithUserOrderByLikeIdDesc(
            @Param("reviewIds") Collection<Long> reviewIds
    );
}
