package com.example.linkingrest.bookmark.service;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.repository.BookmarkRepository;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long saveBookmark(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        Bookmark bookmark = Bookmark.builder().user(user).post(post).build();
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
