package com.example.linkingrest.post.service;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long savePost(Post post, Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        Post savePost = Post.createPost(post,user);
        postRepository.save(savePost);
        return savePost.getId();
    }

    public List<Post> findPostsByType(String type){
        PostType postType = PostType.valueOf(type);
        return postRepository.findByPostType(postType);
    }

    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void updatePost(Post post,Long id){
        Post findPost = postRepository.findById(id).orElseThrow();
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
