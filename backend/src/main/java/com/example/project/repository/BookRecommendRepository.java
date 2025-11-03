package com.example.project.repository;

import com.example.project.entity.BookRecommend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRecommendRepository extends org.springframework.data.jpa.repository.JpaRepository<BookRecommend, Long> {

    // ✅ 유사도 높은 순 → 날짜(일 단위) 최신순 → 생성 시간순
    @Query(value = """
        SELECT *
        FROM book_recommend
        WHERE user_id = :userId
        ORDER BY hybrid_score DESC, DATE(created_at) DESC, created_at DESC
        LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true)
    List<BookRecommend> findSortedRecommendationsByDate(
            @Param("userId") Long userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Query(value = "SELECT COUNT(*) FROM book_recommend WHERE user_id = :userId", nativeQuery = true)
    long countByUserId(@Param("userId") Long userId);
}
