package com.example.project.repository;

import com.example.project.entity.ReadingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReadingGoalRepository extends JpaRepository<ReadingGoal, Long> {

    // 특정 기간에 유효한 목표 조회
    List<ReadingGoal> findByUserIdAndStartDateBeforeAndEndDateAfter(Long userId, LocalDate start, LocalDate end);
}
