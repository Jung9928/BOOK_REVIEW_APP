package com.jung9407.bookreviewapp.exception.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //String exceptionMessage = (String) request.getAttribute("exception");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        //response.setCharacterEncoding("UTF-8");
        String message = "해당 토큰은 유효하지 않습니다.";

        String res = this.convertObjectToJson(message);
        response.getWriter().print(res);
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        return object == null ? null : objectMapper.writeValueAsString(object);
    }
}