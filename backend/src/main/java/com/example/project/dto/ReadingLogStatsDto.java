package com.example.project.dto;

import java.time.LocalDate;

public class ReadingLogStatsDto {

    private Long userId;
    private int totalPages;
    private int totalMinutes;
    private int completedBooks;
    private LocalDate firstLogDate;
    private LocalDate lastLogDate;

    public ReadingLogStatsDto(Long userId, int totalPages, int totalMinutes, int completedBooks,
                              LocalDate firstLogDate, LocalDate lastLogDate) {
        this.userId = userId;
        this.totalPages = totalPages;
        this.totalMinutes = totalMinutes;
        this.completedBooks = completedBooks;
        this.firstLogDate = firstLogDate;
        this.lastLogDate = lastLogDate;
    }

    // Getter / Setter
    public Long getId() { return userId; }
    public int getTotalPages() { return totalPages; }
    public int getTotalMinutes() { return totalMinutes; }
    public int getCompletedBooks() { return completedBooks; }
    public LocalDate getFirstLogDate() { return firstLogDate; }
    public LocalDate getLastLogDate() { return lastLogDate; }
}
