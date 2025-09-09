package com.example.project.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
public class UpdateReviewRequest {
    private String content;
    private int rating;
    private List<String> hashtags;          // 해시태그
    private List<MultipartFile> files;      // 이미지 파일
}