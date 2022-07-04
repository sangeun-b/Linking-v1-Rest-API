package com.example.linkingrest.comment.repository;

import com.example.linkingrest.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select distinct c from Comment c join fetch c.user u where u.id = :userId")
    List<Comment> findByUser(Long userId);
}
