package com.example.linkingrest.bookmark;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.repository.BookmarkRepository;
import com.example.linkingrest.bookmark.service.BookmarkService;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.post.service.PostService;
import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookmarkMockServiceTest {

    @InjectMocks
    private BookmarkService bookmarkService;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("북마크 등록")
    @Transactional
    @Test
    public void save(){
        // given
        User user = User.builder()
                .email("user1@email.com")
                .password("12345")
                .name("user1")
                .img(null)
                .role(Role.ROLE_MENTOR)
                .build();
        Post post = Post.builder()
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .user(user)
                .build();
        Bookmark bookmark = Bookmark.builder().build();
        // mocking
        given(bookmarkRepository.save(any())).willReturn(bookmark);
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
        given(bookmarkRepository.findById(any())).willReturn(Optional.ofNullable(bookmark));

        // when
        Long newBookmarkId = bookmarkService.saveBookmark(user.getId(), post.getId());

        // then
        Bookmark findBookmark = bookmarkRepository.findById(newBookmarkId).orElse(null);
        assertEquals(bookmark.getId(), findBookmark.getId());
    }

    @Nested
    @DisplayName("북마크 조회")
    class find{
        @DisplayName("회원 별 조회")
        @Test
        public void findAllByUser() throws Exception{
            //given
            Bookmark bookmark = Bookmark.builder().build();
            Bookmark[] bookmarks = {bookmark};
            //mocking
            given(bookmarkRepository.findBookmarksByUser(any())).willReturn(List.of(bookmarks));

            //when
            List<Bookmark> bookmarkList = bookmarkService.findBookmarksByUser(1L);

            //then
            assertEquals(1, bookmarkList.size());
            assertEquals(bookmark.getId(),bookmarkList.get(0).getId());

        }
        @DisplayName("공고 별 조회")
        @Test
        public void findAllByPost() throws Exception{
            //given
            Bookmark bookmark = Bookmark.builder().build();
            Bookmark[] bookmarks = {bookmark};
            //mocking
            given(bookmarkRepository.findBookmarkByPost(any())).willReturn(List.of(bookmarks));
            //when
            List<Bookmark> bookmarkList = bookmarkService.findBookmarksByPost(1L);

            //then
            assertEquals(1, bookmarkList.size());
            assertEquals(bookmark.getId(),bookmarkList.get(0).getId());

        }
    }
    @DisplayName("북마크 삭제")
    @Transactional
    @Test
    public void delete() throws Exception{
        //given
        Bookmark bookmark = Bookmark.builder().build();
        // mocking
        given(bookmarkRepository.findById(any())).willReturn(Optional.ofNullable(bookmark));
        doNothing().when(bookmarkRepository).delete(any());

        // when
        bookmarkService.deleteBookmark(bookmark.getId());

        // then
        verify(bookmarkRepository, times(1)).delete(any());


    }


}
