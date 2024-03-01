package com.jung9407.bookreviewapp.model.dto.requestDTO;

import com.jung9407.bookreviewapp.util.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignupRequestDTO {

    private String memberId;
    private String password;
    private String email;
    private MemberRole memberRole;
}
