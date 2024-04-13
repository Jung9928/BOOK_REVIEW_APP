package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MemberDeleteRequestDTO {

    private String memberId;
    private String password;
    private String accessToken;
}
