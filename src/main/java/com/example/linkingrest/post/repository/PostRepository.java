package com.example.linkingrest.post.repository;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingOrContentContaining(String title, String content);
    List<Post> findByPostType(PostType postType);
}
