package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.GroupPostDto;
import com.example.project.dto.request.GroupPostCreateRequest;
import com.example.project.dto.request.GroupPostUpdateRequest;
import com.example.project.service.GroupPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/posts")
@RequiredArgsConstructor
public class GroupPostController {

    private final GroupPostService groupPostService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<ApiResponse<GroupPostDto>> createPost(@PathVariable Long groupId,
                                                                @RequestAttribute Long userId,
                                                                @RequestBody GroupPostCreateRequest req) {
        return ResponseEntity.ok(ApiResponse.success(groupPostService.createPost(groupId, userId, req)));
    }
    
    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<GroupPostDto>> updatePost(@PathVariable Long postId,
                                                                @RequestAttribute Long userId,
                                                                @RequestBody GroupPostUpdateRequest req) {
        return ResponseEntity.ok(ApiResponse.success(groupPostService.updatePost(postId, userId, req)));
    }
    
    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId,
                                                        @RequestAttribute Long userId) {
        groupPostService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // 그룹 피드 (게시글 목록) 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupPostDto>>> getGroupFeed(@PathVariable Long groupId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(groupPostService.getGroupFeed(groupId, page, size)));
    }
    
    // 게시글 좋아요
    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Void>> likePost(@PathVariable Long postId,
                                                      @RequestAttribute Long userId) {
        groupPostService.likePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
    
    // 게시글 좋아요 취소
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Void>> unlikePost(@PathVariable Long postId,
                                                        @RequestAttribute Long userId) {
        groupPostService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}