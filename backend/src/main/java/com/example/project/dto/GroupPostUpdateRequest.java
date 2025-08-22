package com.example.project.dto.request;

import com.example.project.entity.GroupPost;
import lombok.Data;

@Data
public class GroupPostUpdateRequest {
    private GroupPost.PostType postType;
    private String title;
    private String content;
    private String imageUrl;
}
