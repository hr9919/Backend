package com.example.project.dto;

import com.example.project.entity.ReadingGoal;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingGoalDto {

    private Long id;
    private Long userId;
    private ReadingGoal.GoalType goalType;
    private int year;
    private Integer month;

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

    public static ReadingGoalDto fromEntity(ReadingGoal goal) {
        return ReadingGoalDto.builder()
                .id(goal.getId())
                .userId(goal.getUser().getId())
                .goalType(goal.getGoalType())
                .year(goal.getYear())
                .month(goal.getMonth())
                .targetBooks(goal.getTargetBooks())
                .completedBooks(goal.getCompletedBooks())
                .targetReviews(goal.getTargetReviews())
                .completedReviews(goal.getCompletedReviews())
                .targetMinutes(goal.getTargetMinutes())
                .completedMinutes(goal.getCompletedMinutes())
                .bookProgress(goal.getBookProgress())
                .reviewProgress(goal.getReviewProgress())
                .timeProgress(goal.getTimeProgress())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .build();
    }
}
