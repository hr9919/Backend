package com.example.project.repository;

import com.example.project.entity.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    // 특정 사용자(userId)의 독서 기록 조회
    List<ReadingLog> findByUserId(Long userId);

    // 총 페이지 수
    @Query("SELECT COALESCE(SUM(r.pagesRead), 0) FROM ReadingLog r WHERE r.user.id = :userId")
    int sumPagesByUser(@Param("userId") Long userId);

    // 총 독서 시간
    @Query("SELECT COALESCE(SUM(r.minutesRead), 0) FROM ReadingLog r WHERE r.user.id = :userId")
    int sumMinutesByUser(@Param("userId") Long userId);

    // 완료한 책 개수
    @Query("SELECT COUNT(DISTINCT r.book.id) FROM ReadingLog r WHERE r.user.id = :userId AND r.pagesRead >= r.book.totalPages")
    int countCompletedBooksByUser(@Param("userId") Long userId);

    // 최초 기록일
    @Query("SELECT MIN(r.logDate) FROM ReadingLog r WHERE r.user.id = :userId")
    LocalDate findFirstLogDateByUser(@Param("userId") Long userId);

    // 최종 기록일
    @Query("SELECT MAX(r.logDate) FROM ReadingLog r WHERE r.user.id = :userId")
    LocalDate findLastLogDateByUser(@Param("userId") Long userId);
}
