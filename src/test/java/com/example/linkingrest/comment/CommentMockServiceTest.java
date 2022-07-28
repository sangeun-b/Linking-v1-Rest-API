package com.example.linkingrest.comment;

import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.comment.repository.CommentRepository;
import com.example.linkingrest.comment.service.CommentService;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentMockServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @DisplayName("댓글 작성")
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
        Comment comment = Comment.builder()
                .content("추천드립니다!")
                .createdAt(LocalDateTime.now())
                .rate(5)
                .build();
        // mocking
        given(commentRepository.save(any())).willReturn(comment);
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

        // when
        Long newCommentId = commentService.saveComment(comment,user.getId(), post.getId());

        // then
        Comment findComment = commentService.findById(newCommentId);
        assertEquals(comment.getId(), findComment.getId());
    }

    @Nested
    @DisplayName("댓글 조회")
    class find{
        @DisplayName("작성자로 댓글 조회")
        @Test
        public void findAll() throws Exception{
            //given
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
            Comment comment = Comment.builder()
                    .content("추천드립니다!")
                    .createdAt(LocalDateTime.now())
                    .rate(5)
                    .build();
            Post[] posts = {post};
            Comment[] comments = {comment};
            //mocking
            given(commentRepository.findByUser(any())).willReturn(List.of(comments));

            //when
            List<Comment> commentList = commentService.findCommentsByUser(user.getId());

            //then
            assertEquals(1, commentList.size());
            assertEquals(comment.getContent(),commentList.get(0).getContent());

        }

    }
    @DisplayName("글 삭제")
    @Transactional
    @Test
    public void deleteComment() throws Exception{
        //given
        Comment comment = Comment.builder()
                .content("추천드립니다!")
                .createdAt(LocalDateTime.now())
                .rate(5)
                .build();
        // mocking
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));
        doNothing().when(commentRepository).delete(any());
        // when
        commentService.deleteComment(comment.getId());

        // then
//        Post findPost = postRepository.findById(newPostId).orElse(null);
//        assertNull(findPost);
        verify(commentRepository, times(1)).delete(any());
//        assertEquals(post.getId(), findPost.getId());

    }

    @DisplayName("댓글 수정")
    @Transactional
    @Test
    public void updateComment() throws Exception{
        //given
        Comment comment = Comment.builder()
                .content("추천드립니다!")
                .createdAt(LocalDateTime.now())
                .rate(5)
                .build();
        Comment newComment = Comment.builder().content("비추천 합니다").rate(1).build();
        // mocking
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));
        // when
        commentService.updateComment(newComment,comment.getId());
        // then
        Comment findComment = commentRepository.findById(newComment.getId()).orElse(null);
        assertEquals(newComment.getId(), findComment.getId());
        assertEquals(newComment.getContent(),findComment.getContent());
        assertEquals(newComment.getRate(),findComment.getRate());

    }
}
