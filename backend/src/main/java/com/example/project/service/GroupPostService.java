package com.example.project.service;

import com.example.project.dto.GroupPostDto;
import com.example.project.dto.request.GroupPostCreateRequest;
import com.example.project.dto.request.GroupPostUpdateRequest;
import com.example.project.entity.Group;
import com.example.project.entity.GroupLike;
import com.example.project.entity.GroupPost;
import com.example.project.entity.GroupMember;
import com.example.project.entity.User;
import com.example.project.repository.GroupLikeRepository;
import com.example.project.repository.GroupMemberRepository;
import com.example.project.repository.GroupPostRepository;
import com.example.project.repository.GroupRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupPostService {

    private final GroupPostRepository groupPostRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupLikeRepository groupLikeRepository; // GroupLikeRepository 추가

    @Transactional
    public GroupPostDto createPost(Long groupId, Long userId, GroupPostCreateRequest req) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("그룹 멤버만 글을 작성할 수 있습니다."));
        
        if (member.getJoinStatus() != GroupMember.JoinStatus.ACCEPTED) {
            throw new RuntimeException("그룹 가입이 승인되지 않았습니다.");
        }

        GroupPost post = GroupPost.builder()
                .group(group)
                .user(user)
                .postType(req.getPostType())
                .title(req.getTitle())
                .content(req.getContent())
                .imageUrl(req.getImageUrl())
                .build();
        
        return GroupPostDto.fromEntity(groupPostRepository.save(post));
    }
    
    @Transactional
    public GroupPostDto updatePost(Long postId, Long userId, GroupPostUpdateRequest req) {
        GroupPost post = groupPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        post.setPostType(req.getPostType());

        return GroupPostDto.fromEntity(groupPostRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        GroupPost post = groupPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }

        groupPostRepository.delete(post);
    }
    
    @Transactional(readOnly = true)
    public List<GroupPostDto> getGroupFeed(Long groupId, int page, int size) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        return groupPostRepository.findByGroupOrderByCreatedAtDesc(group, PageRequest.of(page, size))
                .stream()
                .map(GroupPostDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 좋아요 기능 추가
    @Transactional
    public void likePost(Long postId, Long userId) {
        GroupPost post = groupPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (groupLikeRepository.findByPostAndUser(post, user).isPresent()) {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }
        
        GroupLike like = GroupLike.builder()
                .post(post)
                .user(user)
                .build();
        
        groupLikeRepository.save(like);
    }
    
    // 좋아요 취소 기능 추가
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        GroupPost post = groupPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        GroupLike like = groupLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new RuntimeException("좋아요를 누르지 않았습니다."));
        
        groupLikeRepository.delete(like);
    }
}