package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.CommentDto;
import com.example.project.dto.CreateCommentRequest;
import com.example.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentDto>> create(@RequestBody CreateCommentRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                commentService.create(req)
        ));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDto>> update(@PathVariable Long commentId,
                                                          @RequestParam Long me,
                                                          @RequestParam String content) {
        return ResponseEntity.ok(ApiResponse.success(
                commentService.update(commentId, me, content)
        ));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long commentId,
                                                    @RequestParam Long me) {
        commentService.delete(commentId, me);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/by-review/{reviewId}")
    public ResponseEntity<ApiResponse<List<CommentDto>>> listByReview(@PathVariable Long reviewId,
                                                                      @RequestParam(required = false) Long me) {
        return ResponseEntity.ok(ApiResponse.success(
                commentService.listByReview(reviewId, me)
        ));
    }
}
