package com.example.project.repository;

import com.example.project.entity.Group;
import com.example.project.entity.GroupMember;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupAndUser(Group group, User user);
    List<GroupMember> findByUserId(Long userId);
    int countByGroup(Group group);
}