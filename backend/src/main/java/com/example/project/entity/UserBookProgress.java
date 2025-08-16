package com.example.project.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_book_progress", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
public class UserBookProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private Integer pagesRead;
    private Boolean completed;
    private LocalDateTime completedAt;

    public UserBookProgress(User user, Book book) {
        this.user = user;
        this.book = book;
        this.pagesRead = 0;
        this.completed = false;
    }
}
