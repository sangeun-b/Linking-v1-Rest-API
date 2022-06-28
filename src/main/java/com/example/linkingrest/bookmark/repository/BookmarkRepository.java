package com.example.linkingrest.bookmark.repository;

import com.example.linkingrest.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findBookmarksByUser(Long userId);
    List<Bookmark> findBookmarkByPost(Long postId);
}
