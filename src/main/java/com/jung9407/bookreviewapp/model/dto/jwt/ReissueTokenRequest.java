package com.jung9407.bookreviewapp.model.dto.jwt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ReissueTokenRequest {

    private final String refreshToken;

    public ReissueTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
