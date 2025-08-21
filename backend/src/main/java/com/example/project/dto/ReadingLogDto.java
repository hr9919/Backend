package com.example.project.dto;

import com.example.project.entity.ReadingLog;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingLogDto {

    private Long id;
    private Long userId;
    private Long bookId;
    private int pagesRead;
    private int minutesRead;
    private LocalDateTime readAt;

    public static ReadingLogDto fromEntity(ReadingLog log) {
        return ReadingLogDto.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .bookId(log.getBook().getId())
                .pagesRead(log.getPagesRead())
                .minutesRead(log.getMinutesRead())
                .readAt(log.getReadAt())
                .build();
    }
}
