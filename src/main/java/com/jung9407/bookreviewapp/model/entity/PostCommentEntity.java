package com.jung9407.bookreviewapp.model.entity;

import java.time.LocalDateTime;

public class PostCommentEntity {

    private Long postId;
    private PostEntity postEntity;
    private String comment;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

}
