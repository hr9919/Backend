package com.example.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDto {
    private int totalPages;
    private int totalMinutes;
    private long completedReviews;
    // ... 필요한 필드 추가
}