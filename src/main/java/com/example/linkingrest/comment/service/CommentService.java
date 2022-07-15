package com.example.linkingrest.comment.service;

import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.comment.repository.CommentRepository;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.repository.PostRepository;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import com.example.linkingrest.error.exception.CommentNotFoundException;
import com.example.linkingrest.error.exception.PostNotFoundException;
import com.example.linkingrest.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long saveComment(Comment comment,Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<Comment> findCommentsByUser(Long userId){
        return commentRepository.findByUser(userId);
    }

    public Comment findById(Long id){
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }
    @Transactional
    public void updateComment(Comment comment,Long id){
        Comment findComment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        findComment.updateComment(comment.getContent(),comment.getRate());
    }
    @Transactional
    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
    }



}
