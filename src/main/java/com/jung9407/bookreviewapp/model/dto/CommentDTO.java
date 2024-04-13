package com.jung9407.bookreviewapp.model.dto;

import com.jung9407.bookreviewapp.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.jung9407.bookreviewapp.util.CommentDeleteStatus.Y;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    private String comment;
    private String memberId;

    private Long postId;

    private List<CommentDTO> childComments = new ArrayList<>();
    private String isDeleted;

    public CommentDTO(Long id, String comment, String memberId, Long postId, String isDeleted) {
        this.id = id;
        this.comment = comment;
        this.memberId = memberId;
        this.postId = postId;
        this.isDeleted = isDeleted;
    }

    public static CommentDTO entityToCommentDTO(CommentEntity commentEntity) {
        return commentEntity.getIsDeleted() == Y ?
                new CommentDTO(commentEntity.getCommentId(), "삭제된 댓글입니다.", null, null, null, Y.name()) :
                new CommentDTO(commentEntity.getCommentId(), commentEntity.getComment(), commentEntity.getMember().getMemberId(), commentEntity.getGeneralForum().getPostId(), commentEntity.getIsDeleted().name());

    }
}
