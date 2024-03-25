package com.jung9407.bookreviewapp.model.dto.jwt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TokenResponseDTO {

    private final String accessToken;
    private final String refreshToken;

    private final String memberId;

    public TokenResponseDTO(String accessToken, String refreshToken, String memberId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }
}
