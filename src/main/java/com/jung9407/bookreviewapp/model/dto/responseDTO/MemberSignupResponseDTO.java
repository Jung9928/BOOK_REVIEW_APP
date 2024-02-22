package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.util.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignupResponseDTO {

    private Long id;
    private String memberId;
    private String email;
    private MemberRole memberRole;

    public static MemberSignupResponseDTO getMemberField(MemberDTO memberDTO) {
        return new MemberSignupResponseDTO(
                memberDTO.getId(),
                memberDTO.getMemberId(),
                memberDTO.getEmail(),
                memberDTO.getMemberRole()
        );
    }
}
