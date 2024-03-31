package com.jung9407.bookreviewapp.model.entity;

import jakarta.persistence.Table;

import java.time.LocalDateTime;

public class GeneralForumCommentEntity {

    private Long postId;
    private GeneralForumEntity generalForumEntity;
    private String comment;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

}
