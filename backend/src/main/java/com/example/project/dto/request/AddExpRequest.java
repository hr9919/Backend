package com.example.project.dto.request;

import lombok.Data;

@Data
public class AddExpRequest {
    private Long userId;
    private int points;
}
