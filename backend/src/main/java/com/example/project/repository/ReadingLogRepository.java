package com.example.project.repository;

import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    List<ReadingLog> findByUser(User user);

    @Query("SELECT COALESCE(SUM(r.pagesRead),0) FROM ReadingLog r WHERE r.user = :user")
    int sumPagesByUser(User user);

    @Query("SELECT COALESCE(SUM(r.minutesRead),0) FROM ReadingLog r WHERE r.user = :user")
    int sumMinutesByUser(User user);

    @Query("SELECT COUNT(r) FROM ReadingLog r WHERE r.user = :user AND r.pagesRead >= r.book.totalPages")
    int countCompletedBooksByUser(User user);

    @Query("SELECT MIN(r.readAt) FROM ReadingLog r WHERE r.user = :user")
    LocalDate findFirstLogDateByUser(User user);

    @Query("SELECT MAX(r.readAt) FROM ReadingLog r WHERE r.user = :user")
    LocalDate findLastLogDateByUser(User user);
}
