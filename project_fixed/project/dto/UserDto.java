package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String nickname;
    private String email;
    private String username;
    private List<String> socialProviders;
    private String token; // JWT 저장
}
