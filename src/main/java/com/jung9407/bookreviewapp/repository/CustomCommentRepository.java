package com.jung9407.bookreviewapp.repository;

import com.jung9407.bookreviewapp.model.entity.CommentEntity;

import java.util.List;

public interface CustomCommentRepository {

    List<CommentEntity> findCommentByGeneralForum(Long postId);
    void updateComment(CommentEntity commentEntity);
}
