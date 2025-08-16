package com.example.project.dto;

import com.example.project.entity.Book;
import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ReadingLogDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate logDate;
    private Integer pagesRead;
    private Integer minutesRead;

    // DTO -> Entity 변환
    public ReadingLog convertToEntity(User user, Book book) {
        if (user == null || book == null) {
            throw new IllegalArgumentException("User 또는 Book이 null입니다.");
        }
        return ReadingLog.builder()
                .id(this.id)
                .user(user)
                .book(book)
                .logDate(this.logDate)
                .pagesRead(this.pagesRead)
                .minutesRead(this.minutesRead)
                .build();
    }

    // Entity -> DTO 변환
    public static ReadingLogDto fromEntity(ReadingLog log) {
        return new ReadingLogDto(
                log.getId(),
                log.getUser().getId(),
                log.getBook().getId(),
                log.getLogDate(),
                log.getPagesRead(),
                log.getMinutesRead()
        );
    }
}
