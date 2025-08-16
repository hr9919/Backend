package com.example.project.repository;

import com.example.project.entity.UserBookProgress;
import com.example.project.entity.User;
import com.example.project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBookProgressRepository extends JpaRepository<UserBookProgress, Long> {
    Optional<UserBookProgress> findByUserAndBook(User user, Book book);
    int countByUserIdAndCompletedTrue(Long userId);
}
