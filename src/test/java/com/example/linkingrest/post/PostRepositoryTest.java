package com.example.linkingrest.post;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

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


    @Transactional
    @DisplayName("글 작성")
    @Test
    void write(){

        postRepository.save(post);
        Long postId = post.getId();
        Post findPost = postRepository.findById(postId).orElse(null);

        assertNotNull(findPost);
        assertEquals(findPost.getId(),postId);
    }

    @Nested
    @DisplayName("글 조회")
    class find{
        @DisplayName("글 전체 조회")
        @Test
        void findAll() {
            postRepository.save(post);
            postRepository.save(post_mentee);
            List<Post> postList = postRepository.findAll();
            assertEquals(2,postList.size());

        }
        @DisplayName("글 카테고리로 조회")
        @Test
        void finById(){
            postRepository.save(post);
            List<Post> postList = postRepository.findByPostType(PostType.FIND_MENTOR);
            assertEquals(1,postList.size());
            postRepository.save(post_mentee);
            List<Post> postList_mentee = postRepository.findByPostType(PostType.FIND_MENTEE);
            assertEquals(1,postList.size());

        }
        @DisplayName("키워드로 검색")
        @Test
        void findByKeyword(){
            postRepository.save(post);
            postRepository.save(post_mentee);
            List<Post> postList = postRepository.findByTitleContainingOrContentContaining("서울","서울");
            assertEquals(1,postList.size());
        }
    }
    @DisplayName("글 삭제")
    @Test
    void deletePost(){
        postRepository.save(post);
        postRepository.delete(post);
        assertEquals(0,postRepository.findAll().size());
    }

}
