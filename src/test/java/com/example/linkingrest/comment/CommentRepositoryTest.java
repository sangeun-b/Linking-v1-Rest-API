package com.example.linkingrest.comment;

import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.comment.repository.CommentRepository;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 실제 db로 테스트
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private final Comment comment = Comment.builder()
            .content("추천드립니다!")
            .createdAt(LocalDateTime.now())
            .rate(5)
            .build();
    private final Comment comment2 = Comment.builder()
            .content("비추천")
            .createdAt(LocalDateTime.now())
            .rate(1)
            .build();

    @DisplayName("댓글 작성")
    @Transactional
    @Test
    public void writeComment() throws Exception{
        //given
        comment.setPost(Post.builder().title("멘티 구합니다").build());
        comment.setUser(User.builder().email("email@email.com").password("12345").build());
        commentRepository.save(comment);

        //when
        Long commentId = comment.getId();
        Comment findComment = commentRepository.findById(commentId).orElse(null);

        //then
        assertNotNull(findComment);
        assertEquals(findComment.getId(),commentId);

    }
    @Nested
    @DisplayName("댓글 조회")
    class find{
        @DisplayName("댓글 전체 조회")
        @Test
        void findAll() {
            commentRepository.save(comment);
            commentRepository.save(comment2);
            List<Comment> commentList = commentRepository.findAll();
            assertEquals(2,commentList.size());

        }
        @DisplayName("회원으로 조회")
        @Test
        void finByUser(){
            User user = User.builder()
                    .email("email@email.com").password("12345").build();
            userRepository.save(user);
            comment.setUser(user);
            comment2.setUser(user);
            commentRepository.save(comment);
            commentRepository.save(comment2);
            List<Comment> commentList = commentRepository.findByUser(user.getId());
            assertEquals(2,commentList.size());

        }
        @DisplayName("게시글로 조회")
        @Test
        void findByPost(){
            Post post = Post.builder()
                            .title("멘토를 구합니다!").build();
            postRepository.save(post);
            comment.setPost(post);
            comment2.setPost(post);
            commentRepository.save(comment);
            commentRepository.save(comment2);
            List<Comment> commentList = commentRepository.findByPost(post.getId());
            assertEquals(2,commentList.size());
        }
    }
    @DisplayName("글 삭제")
    @Transactional
    @Test
    void delete(){
        commentRepository.save(comment);
        commentRepository.delete(comment);
        assertEquals(0,commentRepository.findAll().size());
    }

}
