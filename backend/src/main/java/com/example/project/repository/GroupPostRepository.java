package com.example.project.repository;

import com.example.project.entity.Group;
import com.example.project.entity.GroupPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {
    List<GroupPost> findByGroupOrderByCreatedAtDesc(Group group, Pageable pageable);
}
