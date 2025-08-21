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
    @Column(name = "goal_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false)
    private GoalType goalType;

    @Column(nullable = false)
    private int year;

    @Column
    private Integer month;

    @Column(name = "target_books", nullable = false)
    private int targetBooks;

    @Column(name = "completed_books", nullable = false)
    private int completedBooks;

    @Column(name = "target_reviews")
    private int targetReviews;

    @Column(name = "completed_reviews", nullable = false)
    private int completedReviews;

    @Column(name = "target_minutes", nullable = false)
    private int targetMinutes;

    @Column(name = "completed_minutes", nullable = false)
    private int completedMinutes;

    @Column(name = "book_progress", nullable = false)
    private double bookProgress;

    @Column(name = "review_progress", nullable = false)
    private double reviewProgress;

    @Column(name = "time_progress", nullable = false)
    private double timeProgress;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
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
