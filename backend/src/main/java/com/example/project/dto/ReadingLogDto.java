package com.example.project.dto;

import com.example.project.entity.ReadingLog;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingLogDto {

    private Long id;
    private Long userId;
    private Long bookId;
    private int pagesRead;
    private int minutesRead;
    private LocalDateTime readAt;

    public ReadingLog toEntity(com.example.project.entity.User user, com.example.project.entity.Book book) {
        return ReadingLog.builder()
                .user(user)
                .book(book)
                .pagesRead(this.pagesRead)
                .minutesRead(this.minutesRead)
                .readAt(this.readAt != null ? this.readAt : LocalDateTime.now())
                .build();
    }

    public ReadingLogDto(ReadingLog log) {
        this.id = log.getId();
        this.userId = log.getUser().getId();
        this.bookId = log.getBook().getId();
        this.pagesRead = log.getPagesRead();
        this.minutesRead = log.getMinutesRead();
        this.readAt = log.getReadAt();
    }
}
