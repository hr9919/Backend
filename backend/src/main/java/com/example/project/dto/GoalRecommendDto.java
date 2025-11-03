package com.example.project.dto;

import com.example.project.entity.GoalRecommend;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GoalRecommendDto {
    private Integer recommendedBooks;
    private Integer recommendedMinutes;
    private Integer recommendedReviews;
    private String preferredPeriod;
    private Integer preferredHour;
    private Integer sessionMinutes;
    private Integer daysPerWeek;
    private Integer recommendedWeeklyMinutes;
    private String rationale;
    private Integer daysSinceLastRead;
    private Boolean inactiveFlag;
    private LocalDateTime createdAt;

    public static GoalRecommendDto fromEntity(GoalRecommend e) {
        return GoalRecommendDto.builder()
                .recommendedBooks(e.getRecommendedBooks())
                .recommendedMinutes(e.getRecommendedMinutes())
                .recommendedReviews(e.getRecommendedReviews())
                .preferredPeriod(e.getPreferredPeriod())
                .preferredHour(e.getPreferredHour())
                .sessionMinutes(e.getSessionMinutes())
                .daysPerWeek(e.getDaysPerWeek())
                .recommendedWeeklyMinutes(e.getRecommendedWeeklyMinutes())
                .rationale(e.getRationale())
                .daysSinceLastRead(e.getDaysSinceLastRead())
                .inactiveFlag(e.getInactiveFlag())
                .createdAt(e.getCreatedAt())
                .build();
    }
}