package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostModifyResponseDTO {

    private Long postId;

    private MemberDTO member;

    private String title;

    private String content;

    private Timestamp registeredAt;

    private Timestamp modifiedAt;

    public static PostModifyResponseDTO getPostModifyResponseDTO(PostEntity postEntity) {
        return new PostModifyResponseDTO(
                postEntity.getPostId(),
                MemberDTO.entityToMemberDTO(postEntity.getMember()),
                postEntity.getTitle(),
                postEntity.getContent(),
                postEntity.getRegisteredAt(),
                postEntity.getModifiedAt()
        );
    }
}
