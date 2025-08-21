package com.example.project.repository;

import com.example.project.entity.Feed;
import com.example.project.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("select f from Feed f where f.user in (?1) order by f.createdAt desc")
    List<Feed> findByUserInOrderByCreatedAtDesc(List<User> users, Pageable pageable);
}
