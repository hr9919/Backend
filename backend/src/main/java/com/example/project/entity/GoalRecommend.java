package com.example.project.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goal_recommend")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GoalRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
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
}