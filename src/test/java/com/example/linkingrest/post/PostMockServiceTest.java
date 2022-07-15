package com.example.linkingrest.post;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.post.service.PostService;
import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostMockServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("글 작성")
    @Transactional
    @Test
    public void write(){
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
        // mocking
        given(postRepository.save(any())).willReturn(post);
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));

        // when
        Long newPostId = postService.savePost(post,user.getId());

        // then
        Post findPost = postRepository.findById(newPostId).orElse(null);
        assertEquals(post.getId(), findPost.getId());
    }

    @Nested
    @DisplayName("글 조회")
    class find{
        @DisplayName("멘토 공고 조회")
        @Test
        public void findAllByMentor() throws Exception{
            //given
            Post post = Post.builder()
                    .title("멘토!!")
                    .content("멘토님 구합니다")
                    .price(10000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTOR)
                    .build();
            Post post_mentee = Post.builder()
                    .title("멘티!!")
                    .content("멘티 구합니다! 지역은 서울!")
                    .price(20000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTEE)
                    .build();
            Post[] posts = {post};
            //mocking
            given(postRepository.findByPostType(any())).willReturn(List.of(posts));

            //when
            List<Post> postList = postService.findPostsByType("FIND_MENTOR");

            //then
            assertEquals(1, postList.size());
            assertEquals(post.getPostType(),postList.get(0).getPostType());

        }
        @DisplayName("멘티 공고 조회")
        @Test
        public void findAllByMentee () throws Exception{
            //given
            Post post = Post.builder()
                    .title("멘토!!")
                    .content("멘토님 구합니다")
                    .price(10000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTOR)
                    .build();
            Post post_mentee = Post.builder()
                    .title("멘티!!")
                    .content("멘티 구합니다! 지역은 서울!")
                    .price(20000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTEE)
                    .build();

            Post[] posts = {post_mentee};
            //mocking
            given(postRepository.findByPostType(any())).willReturn(List.of(posts));
            //when
            List<Post> postList = postService.findPostsByType("FIND_MENTEE");

            //then
            assertEquals(1, postList.size());
            assertEquals(post_mentee.getPostType(),postList.get(0).getPostType());

        }
        @DisplayName("키워드 검색")
        @Test
        public void findByKeyword() throws Exception{
            //given
            Post post = Post.builder()
                    .title("멘토!!")
                    .content("멘토님 구합니다")
                    .price(10000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTOR)
                    .build();
            Post post_mentee = Post.builder()
                    .title("멘티!!")
                    .content("멘티 구합니다! 지역은 서울!")
                    .price(20000)
                    .quantity("5회")
                    .postType(PostType.FIND_MENTEE)
                    .build();
            Post[] posts = {post_mentee};
            //mocking
            given(postRepository.findByTitleContainingOrContentContaining(any(),any())).willReturn(List.of(posts));
            //when
            List<Post> postList = postService.findPostByKeyword("서울");

            //then
            assertEquals(1, postList.size());
            assertEquals(post_mentee.getContent(),postList.get(0).getContent());
        }
    }
    @DisplayName("글 삭제")
    @Transactional
    @Test
    public void deletePost() throws Exception{
        //given
        Post post = Post.builder()
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();
        // mocking
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        doNothing().when(postRepository).delete(any());
        // when
        postService.deletePost(post.getId());

        // then
//        Post findPost = postRepository.findById(newPostId).orElse(null);
//        assertNull(findPost);
        verify(postRepository, times(1)).delete(any());
//        assertEquals(post.getId(), findPost.getId());

    }

    @DisplayName("글 수정")
    @Transactional
    @Test
    public void updatePost() throws Exception{
        //given
        Post post = Post.builder()
                .title("멘토!!")
                .content("멘토님 구합니다")
                .price(10000)
                .quantity("5회")
                .postType(PostType.FIND_MENTOR)
                .build();
        Post newPost = Post.builder().title("멘토님 구합니다!").build();
        // mocking
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        // when
        postService.updatePost(newPost,post.getId());
        // then
        Post findPost = postRepository.findById(newPost.getId()).orElse(null);
        assertEquals(newPost.getId(), findPost.getId());
        assertEquals(newPost.getTitle(),findPost.getTitle());

    }

}
