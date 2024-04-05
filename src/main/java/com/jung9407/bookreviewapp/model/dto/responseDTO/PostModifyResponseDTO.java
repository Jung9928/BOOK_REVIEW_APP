package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostModifyResponseDTO {

    private Long postId;

    private MemberDTO member;

    private String title;

    private byte[] content;

    private Timestamp registeredAt;

    private Timestamp modifiedAt;

    public static PostModifyResponseDTO getPostModifyResponseDTO(GeneralForumEntity generalForumEntity) {
        return new PostModifyResponseDTO(
                generalForumEntity.getPostId(),
                MemberDTO.entityToMemberDTO(generalForumEntity.getMember()),
                generalForumEntity.getTitle(),
                generalForumEntity.getContent(),
                generalForumEntity.getRegisteredAt(),
                generalForumEntity.getModifiedAt()
        );
    }
}
