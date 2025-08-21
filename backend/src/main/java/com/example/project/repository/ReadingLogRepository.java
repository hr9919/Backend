package com.example.project.repository;

import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    // 특정 사용자 독서 기록 조회
    List<ReadingLog> findByUser(User user);

    // 특정 사용자 독서 기록 전체 삭제
    void deleteByUser(User user);

    // 총 읽은 페이지 수
    @Query("SELECT COALESCE(SUM(r.pagesRead), 0) FROM ReadingLog r WHERE r.user = :user")
    int sumPagesByUser(@Param("user") User user);

    // 총 읽은 시간(분)
    @Query("SELECT COALESCE(SUM(r.minutesRead), 0) FROM ReadingLog r WHERE r.user = :user")
    int sumMinutesByUser(@Param("user") User user);

    // 완독한 책 권수 (조건: pagesRead >= 책 전체 페이지 수)
    @Query("SELECT COUNT(r) FROM ReadingLog r WHERE r.user = :user AND r.pagesRead >= r.book.itemPage")
    int countCompletedBooksByUser(@Param("user") User user);

    // 첫 독서 기록 날짜
    @Query("SELECT MIN(r.readAt) FROM ReadingLog r WHERE r.user = :user")
    LocalDate findFirstLogDateByUser(@Param("user") User user);

    // 마지막 독서 기록 날짜
    @Query("SELECT MAX(r.readAt) FROM ReadingLog r WHERE r.user = :user")
    LocalDate findLastLogDateByUser(@Param("user") User user);
}
