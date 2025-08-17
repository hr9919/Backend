package com.example.project.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reading_goals")
public class ReadingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
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

    public void updateProgress() {
        bookProgress = (targetBooks > 0) ? ((double) completedBooks / targetBooks) * 100 : 0;
        reviewProgress = (targetReviews > 0) ? ((double) completedReviews / targetReviews) * 100 : 0;
        timeProgress = (targetMinutes > 0) ? ((double) completedMinutes / targetMinutes) * 100 : 0;

        if (bookProgress >= 100 && reviewProgress >= 100 && timeProgress >= 100) {
            this.endDate = LocalDate.now();
        }
    }

    public void completeBook() { this.completedBooks++; updateProgress(); }
    public void completeReview() { this.completedReviews++; updateProgress(); }
    public void addReadingTime(int minutes) { this.completedMinutes += minutes; updateProgress(); }

    public enum GoalType { MONTHLY, YEARLY }
}
