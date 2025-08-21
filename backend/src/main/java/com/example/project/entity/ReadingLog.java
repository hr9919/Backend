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
@Table(name = "reading_logs")
public class ReadingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;   // PK (JSON에도 id로 노출됨)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, referencedColumnName = "book_id")
    private Book book;

    @Column(name = "pages_read")
    private int pagesRead;

    @Column(name = "minutes_read")
    private int minutesRead;

    @Column(name = "read_at")
    private LocalDateTime readAt = LocalDateTime.now();
}
