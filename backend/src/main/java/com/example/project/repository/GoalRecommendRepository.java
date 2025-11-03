package com.example.project.repository;

import com.example.project.entity.GoalRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GoalRecommendRepository extends JpaRepository<GoalRecommend, Long> {
    Optional<GoalRecommend> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
}