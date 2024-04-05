package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.util.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MemberModifyResponseDTO {

    private Long id;
    private String memberId;
    private String email;
    private String password;
    private MemberRole memberRole;

    public static MemberModifyResponseDTO getMemberField(MemberDTO memberDTO) {
        return new MemberModifyResponseDTO(
                memberDTO.getId(),
                memberDTO.getMemberId(),
                memberDTO.getEmail(),
                memberDTO.getMemberId(),
                memberDTO.getMemberRole()
        );
    }
}
