package com.example.linkingrest.post.repository;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingOrContentContaining(String title, String content);
    @Query("select distinct p from Post p where p.postType = :postType")
    List<Post> findByPostType(PostType postType);
}
