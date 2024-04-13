package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {

    private String content;
    private Long postId;
    private String memberId;
    private Long parentId;
    private String isDeleted;
}
