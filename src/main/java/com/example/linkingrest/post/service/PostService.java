package com.example.linkingrest.post.service;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long savePost(Post post, Long userId){
        User user = userRepository.findById(userId).orElse(null);
        Post savePost = Post.createPost(post,user);
        postRepository.save(savePost);
        return savePost.getId();
    }

    public List<Post> findPostsByType(PostType postType){
        return postRepository.findByPostType(postType);
    }
    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow();
    }

}
