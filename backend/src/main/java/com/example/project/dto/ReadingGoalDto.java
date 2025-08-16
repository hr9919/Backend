package com.example.project.dto;

import com.example.project.entity.ReadingGoal;
import com.example.project.entity.ReadingGoal.GoalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReadingGoalDto {

    private Long id;
    private Long userId;
    private GoalType goalType;

    private int targetBooks;
    private int completedBooks;

    private int targetReviews;
    private int completedReviews;

    private int targetMinutes;
    private int completedMinutes;

    private double bookProgress;
    private double reviewProgress;
    private double timeProgress;

    private LocalDate startDate;
    private LocalDate endDate;

    // 엔티티 → DTO 변환
    public static ReadingGoalDto fromEntity(ReadingGoal goal) {
        ReadingGoalDto dto = new ReadingGoalDto();
        dto.setId(goal.getId());
        dto.setUserId(goal.getUser().getId());
        dto.setGoalType(goal.getGoalType());
        dto.setTargetBooks(goal.getTargetBooks());
        dto.setCompletedBooks(goal.getCompletedBooks());
        dto.setTargetReviews(goal.getTargetReviews());
        dto.setCompletedReviews(goal.getCompletedReviews());
        dto.setTargetMinutes(goal.getTargetMinutes());
        dto.setCompletedMinutes(goal.getCompletedMinutes());
        dto.setBookProgress(goal.getBookProgress());
        dto.setReviewProgress(goal.getReviewProgress());
        dto.setTimeProgress(goal.getTimeProgress());
        dto.setStartDate(goal.getStartDate());
        dto.setEndDate(goal.getEndDate());
        return dto;
    }

    // DTO → 엔티티 변환
    public ReadingGoal toEntity(com.example.project.entity.User user) {
        ReadingGoal goal = new ReadingGoal();
        goal.setUser(user);
        goal.setGoalType(this.goalType);
        goal.setTargetBooks(this.targetBooks);
        goal.setCompletedBooks(this.completedBooks);
        goal.setTargetReviews(this.targetReviews);
        goal.setCompletedReviews(this.completedReviews);
        goal.setTargetMinutes(this.targetMinutes);
        goal.setCompletedMinutes(this.completedMinutes);
        goal.setBookProgress(this.bookProgress);
        goal.setReviewProgress(this.reviewProgress);
        goal.setTimeProgress(this.timeProgress);
        goal.setStartDate(this.startDate);
        goal.setEndDate(this.endDate);
        return goal;
    }
}
