package com.example.linkingrest.bookmark.service;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.repository.BookmarkRepository;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import com.example.linkingrest.error.exception.BookmarkNotFoundException;
import com.example.linkingrest.error.exception.PostNotFoundException;
import com.example.linkingrest.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long saveBookmark(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Bookmark bookmark = Bookmark.builder().user(user).post(post).build();
        log.info("bookmark's user: ",bookmark.getUser());
        log.info("bookmark's post: ",bookmark.getPost());
        bookmark.setPost(post);
        bookmark.setUser(user);
        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    @Transactional
    public void deleteBookmark(Long id){
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(BookmarkNotFoundException::new);
        bookmarkRepository.delete(bookmark);
    }

    public List<Bookmark> findBookmarksByUser(Long userId){
        return bookmarkRepository.findBookmarksByUser(userId);
    }
    public List<Bookmark> findBookmarksByPost(Long postId){
        return bookmarkRepository.findBookmarkByPost(postId);
    }
}
