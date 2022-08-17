package com.example.linkingrest.bookmark;

import com.example.linkingrest.bookmark.controller.BookmarkController;
import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.bookmark.service.BookmarkService;
import com.example.linkingrest.comment.controller.CommentController;
import com.example.linkingrest.comment.controller.CreateCommentRequest;
import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.comment.service.CommentService;
import com.example.linkingrest.config.security.CustomUserDetailsService;
import com.example.linkingrest.config.security.JwtProvider;
import com.example.linkingrest.config.security.service.SecurityService;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.service.PostService;
import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = UserController.class,
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@WebMvcTest(controllers = BookmarkController.class,
        includeFilters = {@ComponentScan.Filter(classes = EnableWebSecurity.class)})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Import({JwtProvider.class})
public class BookmarkControllerTest {

    @MockBean
    private BookmarkService bookmarkService;
    @MockBean
    private UserService userService;
    @MockBean
    private PostService postService;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("댓글 작성")
    @Test
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    public void saveBookmark() throws Exception{
        User user = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .role(Role.ROLE_MENTOR)
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .post(post)
                .build();

        final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/bookmarks/new").param("userId","1")
                        .param("postId","1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(bookmark)))
                .andExpect(status().isCreated()).andDo(print());


    }


    @DisplayName("회원 별 북마크 조회")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void findByUser() throws Exception {
        User user = User.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .role(Role.ROLE_MENTOR)
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();
        Post post2 = Post.builder()
                .id(1L)
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .post(post)
                .build();
        Bookmark bookmark2 = Bookmark.builder()
                .user(user)
                .post(post2)
                .build();
        Bookmark[] bookmarks = {bookmark,bookmark2};

        given(bookmarkService.findBookmarksByUser(any())).willReturn(List.of(bookmarks));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/bookmarks/users/1")).andExpect(status().isOk())
                .andExpect(jsonPath("count").value(2))
                .andDo(print());


    }

    @DisplayName("북마크 삭제")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void delete() throws Exception{
        User user = User.builder()
                .name("user1")
                .email("user1@email.com")
                .password("123")
                .role(Role.ROLE_MENTOR)
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .post(post)
                .build();
        given(bookmarkService.saveBookmark(any(),any())).willReturn(bookmark.getId());
        doNothing().when(bookmarkService).deleteBookmark(bookmark.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/bookmarks/1")).andExpect(status().isNoContent()).andDo(print());


    }

}
