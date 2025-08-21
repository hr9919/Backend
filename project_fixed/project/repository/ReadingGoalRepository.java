package com.example.project.repository;

import com.example.project.entity.ReadingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReadingGoalRepository extends JpaRepository<ReadingGoal, Long> {

    List<ReadingGoal> findByUserIdAndStartDateBeforeAndEndDateAfter(Long userId, LocalDate start, LocalDate end);
}
