package com.example.linkingrest.bookmark.repository;

import com.example.linkingrest.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("select distinct b from Bookmark b join fetch b.user u where u.id = :userId")
    List<Bookmark> findBookmarksByUser(Long userId);
    @Query("select distinct b from Bookmark b join fetch b.post p where p.id = :postId")
    List<Bookmark> findBookmarkByPost(Long postId);
}
