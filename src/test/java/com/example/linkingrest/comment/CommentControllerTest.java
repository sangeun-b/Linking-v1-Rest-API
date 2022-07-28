package com.example.linkingrest.comment;

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
import com.example.linkingrest.user.controller.CreateUserRequest;
import com.example.linkingrest.user.controller.UserController;
import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = UserController.class,
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@WebMvcTest(controllers = CommentController.class,
        includeFilters = {@ComponentScan.Filter(classes = EnableWebSecurity.class)})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Import({JwtProvider.class})
public class CommentControllerTest {

    @MockBean
    private CommentService commentService;
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
    public void saveComment() throws Exception{
        //given
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
        CreateCommentRequest createCommentRequest = CreateCommentRequest.builder()
                .content("추천드립니다!")
                .rate(5)
                .userId(1L)
                .postId(1L)
                .build();
        Comment comment = createCommentRequest.toEntity();
//        given(commentService.saveComment(any(),any(),any())).willReturn(comment.getId());
//        given(commentService.findById(any())).willReturn(comment);

        //when
        final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/comments/new")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(createCommentRequest)))
                .andExpect(status().isCreated()).andDo(print());


        //then
//        assertEquals(comment.getContent(),commentService.findById(1L).getContent());

    }


    @DisplayName("회원 별 댓글 조회")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void findByUser() throws Exception {
        //given
        User user1 = User.builder()
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
        Comment comment = Comment.builder()
                .content("추천드립니다!")
                .rate(5)
                .user(user1)
                .post(post)
                .build();
        Comment comment2 = Comment.builder()
                .content("비추천!")
                .rate(1)
                .user(user1)
                .post(post)
                .build();
        Comment[] commentList = {comment,comment2};
//        commentService.saveComment(comment,1L,1L);
        given(commentService.findCommentsByUser(any())).willReturn(List.of(commentList));

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/comments/users/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].content").value("추천드립니다!"))
                .andDo(print());

//        assertEquals(2,userService.findUsers().size());


    }

    @DisplayName("댓글 삭제")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void delete() throws Exception{
        //given
        Comment comment = Comment.builder()
                .content("추천드립니다!")
                .rate(5)
                .build();
        given(commentService.saveComment(any(),any(),any())).willReturn(comment.getId());
        doNothing().when(commentService).deleteComment(comment.getId());
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/1")).andExpect(status().isNoContent()).andDo(print());

        //then
        //verify(userService,times(1)).deleteUser(1L);

    }
    @DisplayName("댓글 수정")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void update() throws Exception{
        //given
        Comment comment = Comment.builder()
                .content("추천드립니다!")
                .rate(5)
                .build();
//        given(commentService.findById(any())).willReturn(comment);
        given(commentService.saveComment(any(),any(),any())).willReturn(comment.getId());
        given(userService.findById(any())).willReturn(User.builder().id(1L).name("user1").build());
        given(postService.findPostById(any())).willReturn(Post.builder().id(1L).content("멘토 찾기!").build());
        commentService.saveComment(comment,1L,1L);
        Comment newComment = Comment.builder()
                .content("추천!")
                .rate(5)
                .build();
        given(commentService.findById(any())).willReturn(newComment);
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newComment))).andExpect(status().isOk()).andDo(print());

//        //then
//        assertEquals(user.getName(),userService.findById(user.getId()).getName());

    }
}
