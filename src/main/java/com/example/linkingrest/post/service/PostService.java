package com.example.linkingrest.post.service;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import com.example.linkingrest.error.exception.PostNotFoundException;
import com.example.linkingrest.error.exception.UserNotFoundException;
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
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post savePost = Post.createPost(post,user);
        postRepository.save(savePost);
        return savePost.getId();
    }

    public List<Post> findPostsByType(String type){
        PostType postType = PostType.valueOf(type);
        return postRepository.findByPostType(postType);
    }

    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public void updatePost(Post post,Long id){
        Post findPost = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        findPost.updatePost(post.getTitle(),post.getContent(),post.getPrice(),post.getQuantity());
    }
    @Transactional
    public void deletePost(Long id){
        Post post = findPostById(id);
        postRepository.delete(post);
    }
    public List<Post> findPostByKeyword(String keyword){
        return postRepository.findByTitleContainingOrContentContaining(keyword,keyword);

    }


}
