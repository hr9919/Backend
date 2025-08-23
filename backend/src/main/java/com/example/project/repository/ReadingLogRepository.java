package com.example.project.repository;

import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    List<ReadingLog> findByUser(User user);
    void deleteByUser(User user);

    @Query("SELECT COALESCE(SUM(r.pagesRead), 0) FROM ReadingLog r WHERE r.user = :user")
    int sumPagesByUser(@Param("user") User user);
    @Query("SELECT COALESCE(SUM(r.minutesRead), 0) FROM ReadingLog r WHERE r.user = :user")
    int sumMinutesByUser(@Param("user") User user);
    @Query("SELECT COUNT(r) FROM ReadingLog r WHERE r.user = :user AND r.pagesRead >= r.book.itemPage")
    int countCompletedBooksByUser(@Param("user") User user);
    @Query("SELECT MIN(r.readAt) FROM ReadingLog r WHERE r.user = :user")
    LocalDate findFirstLogDateByUser(@Param("user") User user);
    @Query("SELECT MAX(r.readAt) FROM ReadingLog r WHERE r.user = :user")
    LocalDate findLastLogDateByUser(@Param("user") User user);
    
    List<ReadingLog> findByUserAndReadAtBetween(User user, LocalDateTime start, LocalDateTime end);
    @Query("SELECT COALESCE(SUM(r.pagesRead), 0) FROM ReadingLog r WHERE r.user = :user AND r.readAt BETWEEN :start AND :end")
    int sumPagesByUserAndPeriod(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    @Query("SELECT COALESCE(SUM(r.minutesRead), 0) FROM ReadingLog r WHERE r.user = :user AND r.readAt BETWEEN :start AND :end")
    int sumMinutesByUserAndPeriod(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    List<ReadingLog> findByUserOrderByReadAtDesc(User user, Pageable pageable);
}
