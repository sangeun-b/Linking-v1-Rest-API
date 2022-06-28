package com.example.linkingrest.bookmark.service;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Long saveBookmark(Bookmark bookmark){
        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    @Transactional
    public void deleteBookmark(Long id){
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow();
        bookmarkRepository.delete(bookmark);
    }

    public List<Bookmark> findBookmarksByUser(Long userId){
        return bookmarkRepository.findBookmarksByUser(userId);
    }
    public List<Bookmark> findBookmarksByPost(Long postId){
        return bookmarkRepository.findBookmarkByPost(postId);
    }
}
