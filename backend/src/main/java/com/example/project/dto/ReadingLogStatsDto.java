package com.example.project.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingLogStatsDto {
    private Long userId;
    private int totalPagesRead;
    private int totalMinutesRead;
    private int completedBooksCount;
    private LocalDate firstLogDate;
    private LocalDate lastLogDate;
}
