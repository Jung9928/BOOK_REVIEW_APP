package com.jung9407.bookreviewapp.model.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseResultCode<T> {

    private String resultCode;
    private T result;

    // 어떠한 작업을 실패할 경우
    public static ResponseResultCode<Void> error(String errorCode) {
        return new ResponseResultCode<>(errorCode, null);
    }

    // 어떠한 작업을 성공할 경우 (회원가입, 게시글 작성, 삭제, 수정 등)
    // 각각 결과값이 다른 타입을 가지게끔 설계할 것이므로 제네릭(T) 타입 사용하여 다양한 반환 값을 처리하도록 유연성 높임.
    public static <T> ResponseResultCode<T> success(T result) {
        return new ResponseResultCode<>("SUCCESS", null);
    }
}