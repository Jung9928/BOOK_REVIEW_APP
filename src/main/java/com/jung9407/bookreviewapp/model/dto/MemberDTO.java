package com.jung9407.bookreviewapp.model.dto;

import com.jung9407.bookreviewapp.model.entity.MemberEntity;
import com.jung9407.bookreviewapp.util.MemberRole;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
// 회원가입 시, request body로 데이터를 받는 용도의 클래스
public class MemberDTO {

    private Long id;
    private String memberId;
    private String password;
    private String email;
    private MemberRole memberRole;
    private Timestamp registeredAt;
    private Timestamp modifiedAt;

    // MemberEntity에 있는 필드들을 MemberDTO 클래스로 변환하는 메소드
    public static MemberDTO entityToMemberDTO(MemberEntity memberEntity) {
        return new MemberDTO(
                memberEntity.getId(),
                memberEntity.getMemberId(),
                memberEntity.getPassword(),
                memberEntity.getEmail(),
                memberEntity.getMemberRole(),
                memberEntity.getRegisteredAt(),
                memberEntity.getModifiedAt()
        );
    }
}
