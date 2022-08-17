package com.example.linkingrest.post;

import com.example.linkingrest.config.security.CustomUserDetailsService;
import com.example.linkingrest.config.security.JwtProvider;
import com.example.linkingrest.config.security.service.SecurityService;
import com.example.linkingrest.post.controller.CreatePostRequest;
import com.example.linkingrest.post.controller.PostController;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.service.PostService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.AbstractDocument;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class,
        includeFilters = {@ComponentScan.Filter(classes = EnableWebSecurity.class)})
@Import({JwtProvider.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @MockBean
    private PostService postService;
    @MockBean
    private UserService userService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private SecurityService securityService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private final Post post = Post.builder()
            .title("멘토!!")
            .content("멘토님 구합니다")
            .price(10000)
            .quantity("5회")
            .postType(PostType.FIND_MENTOR)
            .build();
    private final Post post_mentee = Post.builder()
            .title("멘티!!")
            .content("멘티 구합니다! 지역은 서울!")
            .price(20000)
            .quantity("5회")
            .postType(PostType.FIND_MENTEE)
            .build();

    @DisplayName("글 작성")
    @WithMockUser(username="test", password = "test_password", roles={"MENTOR"})
    @Test
    void savePost() throws Exception {
        //given
        User user = User.builder()
                .email("user1@email.com")
                .password("12345")
                .name("user1")
                .img(null)
                .role(Role.ROLE_MENTOR)
                .build();
        CreatePostRequest createPostRequest = CreatePostRequest.builder()
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .userId(user.getId())
                .build();
        Post post = createPostRequest.toEntity();
//        given(postService.savePost(any(),any())).willReturn(post.getId());

        //when
        final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/posts/new")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(createPostRequest)));

        //then
        actions.andDo(print())
                .andExpect(status().isCreated());
    }

    @Nested
    @DisplayName("공고 조회 테스트")
    @WithMockUser(username = "test",password = "123",roles = {"ADMIN"})
    class find {
        @DisplayName("멘토 공고 조회")
        @Test
        public void findByMentor() throws Exception {
            //given
            User user = User.builder()
                    .email("user1@email.com")
                    .password("12345")
                    .name("user1")
                    .img(null)
                    .role(Role.ROLE_MENTOR)
                    .build();
            Post post_mentor = Post.builder()
                    .title("멘토!!")
                    .content("멘토님 구합니다")
                    .price(10000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTOR)
                    .user(user)
                    .build();
            Post post_mentor2 = Post.builder()
                    .title("멘토님!!")
                    .content("멘토 구합니다! 지역은 서울!")
                    .price(20000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTOR)
                    .user(user)
                    .build();

            Post[] posts = {post_mentor,post_mentor2};
            given(postService.findPostsByType(any())).willReturn(List.of(posts));
            //when & then
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/posts/category/mentor")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.posts[0].title").value("멘토!!"))
                    .andExpect(jsonPath("$.posts[1].title").value("멘토님!!"))
                    .andDo(print());

        }

        @DisplayName("공고 ID로 조회")
        @Test
        public void findById() throws Exception {
            //given
            User user = User.builder()
                    .email("user1@email.com")
                    .password("12345")
                    .name("user1")
                    .img(null)
                    .role(Role.ROLE_MENTOR)
                    .build();
            Post post= Post.builder()
                    .id(1L)
                    .title("멘토!!")
                    .content("멘토님 구합니다")
                    .price(10000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTOR)
                    .user(user)
                    .build();
            given(postService.findPostById(any())).willReturn(post);
            //when
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/posts/1")).andExpect(status().isOk()).andExpect(jsonPath("title").value("멘토!!")).andDo(print());

        }
    }
    @DisplayName("글 삭제")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void delete() throws Exception{
        //given
        Post post_mentor = Post.builder()
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();
        given(postService.savePost(any(),any())).willReturn(post_mentor.getId());
        doNothing().when(postService).deletePost(post_mentor.getId());
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/1")).andExpect(status().isNoContent()).andDo(print());

        //then
        //verify(postService,times(1)).deletePost(1L);

    }
    @DisplayName("글 수정")
    @WithMockUser(username = "test",password = "123",roles = {"MENTOR"})
    @Test
    public void update() throws Exception{
        Post post_mentor = Post.builder()
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();

        given(postService.savePost(any(),any())).willReturn(post_mentor.getId());
        postService.savePost(post_mentor,1L);
        Post newPost =  Post.builder()
                .title("멘토님!!")
                .content("멘토님 구합니다. 지역은 서울!!")
                .price(20000)
                .quantity("4회")
                .postType(PostType.FIND_MENTOR)
                .build();
        given(postService.findPostById(any())).willReturn(newPost);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPost))).andExpect(status().isOk()).andDo(print());



    }

}
