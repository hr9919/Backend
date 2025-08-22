package com.example.project.service;

import com.example.project.dto.GroupDto;
import com.example.project.dto.request.GroupCreateRequest;
import com.example.project.entity.Group;
import com.example.project.entity.GroupMember;
import com.example.project.entity.User;
import com.example.project.repository.GroupMemberRepository;
import com.example.project.repository.GroupRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public GroupDto createGroup(GroupCreateRequest req, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = Group.builder()
                .name(req.getName())
                .description(req.getDescription())
                .groupImageUrl(req.getGroupImageUrl())
                .category(req.getCategory())
                .owner(owner)
                .build();

        Group savedGroup = groupRepository.save(group);

        GroupMember ownerMember = GroupMember.builder()
                .group(savedGroup)
                .user(owner)
                .joinStatus(GroupMember.JoinStatus.ACCEPTED)
                .build();
        groupMemberRepository.save(ownerMember);

        return GroupDto.fromEntity(savedGroup, 1);
    }
    
    @Transactional
    public GroupDto updateGroup(Long groupId, Long userId, GroupCreateRequest req) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getOwner().getId().equals(userId)) {
            throw new RuntimeException("그룹장만 그룹 정보를 수정할 수 있습니다.");
        }

        group.setName(req.getName());
        group.setDescription(req.getDescription());
        group.setGroupImageUrl(req.getGroupImageUrl());
        group.setCategory(req.getCategory());

        return GroupDto.fromEntity(groupRepository.save(group), groupMemberRepository.countByGroup(group));
    }

    @Transactional
    public void deleteGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getOwner().getId().equals(userId)) {
            throw new RuntimeException("그룹장만 그룹을 삭제할 수 있습니다.");
        }
        
        groupRepository.delete(group);
    }

    @Transactional
    public void requestToJoin(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (groupMemberRepository.findByGroupAndUser(group, user).isPresent()) {
            throw new RuntimeException("이미 그룹에 가입 요청했거나 가입된 상태입니다.");
        }

        GroupMember newMember = GroupMember.builder()
                .group(group)
                .user(user)
                .joinStatus(GroupMember.JoinStatus.PENDING)
                .build();
        groupMemberRepository.save(newMember);
    }

    @Transactional
    public void acceptJoinRequest(Long groupId, Long ownerId, Long memberUserId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, userRepository.findById(memberUserId).orElseThrow())
                .orElseThrow(() -> new RuntimeException("가입 요청을 찾을 수 없습니다."));

        member.setJoinStatus(GroupMember.JoinStatus.ACCEPTED);
        groupMemberRepository.save(member);
    }

    @Transactional
    public void delegateOwner(Long groupId, Long currentOwnerId, Long newOwnerId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getOwner().getId().equals(currentOwnerId)) {
            throw new RuntimeException("그룹장만 권한을 위임할 수 있습니다.");
        }

        User newOwner = userRepository.findById(newOwnerId)
                .orElseThrow(() -> new RuntimeException("새로운 그룹장을 찾을 수 없습니다."));

        GroupMember newOwnerMember = groupMemberRepository.findByGroupAndUser(group, newOwner)
                .orElseThrow(() -> new RuntimeException("새로운 그룹장은 현재 그룹의 멤버여야 합니다."));

        group.setOwner(newOwner);
        groupRepository.save(group);
    }

    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (group.getOwner().getId().equals(userId)) {
            throw new RuntimeException("그룹장은 그룹을 탈퇴할 수 없습니다. 그룹을 삭제하거나 그룹장 권한을 위임하세요.");
        }

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("그룹 멤버가 아닙니다."));

        groupMemberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<GroupDto> searchGroups(String keyword, String category) {
        List<Group> groups;
        if (keyword != null && !keyword.isEmpty()) {
            groups = groupRepository.findByNameContainingIgnoreCase(keyword);
        } else if (category != null && !category.isEmpty()) {
            groups = groupRepository.findByCategory(category);
        } else {
            groups = groupRepository.findAll();
        }

        return groups.stream()
                .map(g -> GroupDto.fromEntity(g, groupMemberRepository.countByGroup(g)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupDto getGroupProfile(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        int memberCount = groupMemberRepository.countByGroup(group);
        return GroupDto.fromEntity(group, memberCount);
    }
}
