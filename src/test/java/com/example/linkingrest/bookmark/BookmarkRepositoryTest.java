package com.example.linkingrest.bookmark;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.repository.BookmarkRepository;
import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @DisplayName("북마크 등록")
    @Transactional
    @Test
    public void save() throws Exception{
        //given
        Bookmark bookmark = Bookmark.builder().build();
        bookmarkRepository.save(bookmark);

        //when
        Long bookmarkId = bookmark.getId();
        Bookmark findBookmark = bookmarkRepository.findById(bookmarkId).orElse(null);
        //then
        assertNotNull(findBookmark);
        assertEquals(findBookmark.getId(),bookmarkId);

    }

    @DisplayName("회원으로 북마크 조회")
    @Test
    void finByUser(){
        User user = User.builder()
                .email("email@email.com").password("12345").build();
        userRepository.save(user);
        Bookmark bookmark = Bookmark.builder().build();
        bookmark.setUser(user);
        bookmarkRepository.save(bookmark);
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarksByUser(user.getId());
        assertEquals(1,bookmarkList.size());

    }

    @DisplayName("글 삭제")
    @Transactional
    @Test
    void delete(){
        Bookmark bookmark = Bookmark.builder().build();
        bookmarkRepository.save(bookmark);
        bookmarkRepository.delete(bookmark);
        assertEquals(0,bookmarkRepository.findAll().size());
    }
}
