package com.jung9407.bookreviewapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_MEMBER_ID(HttpStatus.CONFLICT, "회원가입하려는 아이디가 이미 존재합니다."),     // memberId 중복 에러코드

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "작성하신 아이디는 회원가입이 가능한 아이디 입니다."),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀립니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러 발생"),
    ;

    private HttpStatus httpStatus;
    private String message;
}
