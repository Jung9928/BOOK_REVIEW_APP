package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.model.entity.GeneralForumEntity;
import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralForumResponseDTO {

    private long post_id;

    private String member_id;

    private String title;

    private String content;

    private int vw_cnt;

    private int rcmnd_cnt;

    private Timestamp registeredAt;

    private Timestamp modifiedAt;

    // MemberEntity에 있는 필드들을 MemberDTO 클래스로 변환하는 메소드
    public static GeneralForumResponseDTO entityToMemberDTO(GeneralForumEntity generalForumEntity) {
        return new GeneralForumResponseDTO(
                generalForumEntity.getPostId(),
                generalForumEntity.getMember().getMemberId(),
                generalForumEntity.getTitle(),
                generalForumEntity.getContent().toString(),
                generalForumEntity.getViewCount(),
                0,
                generalForumEntity.getRegisteredAt(),
                generalForumEntity.getModifiedAt()
        );
    }
}
