package com.example.linkingrest.comment.service;

import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long saveComment(Comment comment){
        commentRepository.save(comment);
        return comment.getId();
    }

}
