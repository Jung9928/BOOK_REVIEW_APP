package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginRequestDTO {

    private String userId;
    private String password;
}
