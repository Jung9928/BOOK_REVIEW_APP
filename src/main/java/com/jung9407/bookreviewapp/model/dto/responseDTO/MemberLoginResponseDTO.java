package com.jung9407.bookreviewapp.model.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginResponseDTO {

    private String accessToken;       // 로그인 시, 정상적으로 이루어 진 경우 반환되는 액세스 토큰 값 저장 변수
    private String refreshToken;       // 로그인 시, 정상적으로 이루어 진 경우 반환되는 리프레쉬 토큰 값 저장 변수
}
