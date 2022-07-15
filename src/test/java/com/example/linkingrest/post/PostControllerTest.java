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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        given(postService.savePost(any(),any())).willReturn(post.getId());

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

}
