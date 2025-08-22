package com.example.project.repository;

import com.example.project.entity.GroupLike;
import com.example.project.entity.GroupPost;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupLikeRepository extends JpaRepository<GroupLike, Long> {
    Optional<GroupLike> findByPostAndUser(GroupPost post, User user);
    int countByPost(GroupPost post);
}
