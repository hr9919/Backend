package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.GroupDto;
import com.example.project.dto.request.GroupCreateRequest;
import com.example.project.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    
    // 그룹 생성
    @PostMapping
    public ResponseEntity<ApiResponse<GroupDto>> createGroup(@RequestBody GroupCreateRequest req,
                                                             @RequestAttribute Long userId) {
        return ResponseEntity.ok(ApiResponse.success(groupService.createGroup(req, userId)));
    }
    
    // 그룹 수정
    @PutMapping("/{groupId}")
    public ResponseEntity<ApiResponse<GroupDto>> updateGroup(@PathVariable Long groupId,
                                                             @RequestAttribute Long userId,
                                                             @RequestBody GroupCreateRequest req) {
        return ResponseEntity.ok(ApiResponse.success(groupService.updateGroup(groupId, userId, req)));
    }

    // 그룹 삭제
    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long groupId,
                                                         @RequestAttribute Long userId) {
        groupService.deleteGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    // 그룹 참여 요청
    @PostMapping("/{groupId}/join")
    public ResponseEntity<ApiResponse<Void>> requestToJoin(@PathVariable Long groupId,
                                                           @RequestAttribute Long userId) {
        groupService.requestToJoin(groupId, userId);
        return ResponseEntity.noContent().build();
    }
    
    // 그룹 가입 요청 승인 (그룹장 전용)
    @PostMapping("/{groupId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptJoinRequest(@PathVariable Long groupId,
                                                               @RequestAttribute Long userId,
                                                               @RequestParam Long memberUserId) {
        groupService.acceptJoinRequest(groupId, userId, memberUserId);
        return ResponseEntity.noContent().build();
    }
    
    // 그룹장 위임
    @PutMapping("/{groupId}/delegate-owner")
    public ResponseEntity<ApiResponse<Void>> delegateOwner(@PathVariable Long groupId,
                                                           @RequestAttribute Long userId,
                                                           @RequestParam Long newOwnerId) {
        groupService.delegateOwner(groupId, userId, newOwnerId);
        return ResponseEntity.noContent().build();
    }

    // 그룹 탈퇴
    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveGroup(@PathVariable Long groupId,
                                                        @RequestAttribute Long userId) {
        groupService.leaveGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    // 그룹 검색 (키워드 또는 카테고리)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<GroupDto>>> searchGroups(@RequestParam(required = false) String keyword,
                                                                    @RequestParam(required = false) String category) {
        return ResponseEntity.ok(ApiResponse.success(groupService.searchGroups(keyword, category)));
    }

    // 그룹 프로필 조회
    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse<GroupDto>> getGroupProfile(@PathVariable Long groupId) {
        return ResponseEntity.ok(ApiResponse.success(groupService.getGroupProfile(groupId)));
    }
}
