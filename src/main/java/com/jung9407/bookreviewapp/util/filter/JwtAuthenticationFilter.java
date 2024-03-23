package com.jung9407.bookreviewapp.util.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jung9407.bookreviewapp.exception.GlobalControllerAdvice;
import com.jung9407.bookreviewapp.model.dao.RedisDAO;
import com.jung9407.bookreviewapp.util.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisDAO redisDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String token = jwtProvider.getTokenFromHeader(request);
        log.info("token : " + token);

        if(token != null) {
            String blackList = redisDAO.getBlackList(token);
            log.info("blackList : " + blackList);

            if(blackList != null) {
                if(blackList.equals("logout")) {
                    throw new IllegalArgumentException("다시 로그인을 진행해 주시기 바랍니다.");
                }
            }

            if(!jwtProvider.validateToken(token)) {
                response.sendError(401, "토큰이 만료되었습니다.");
                jwtExceptionHandler(response, "401", HttpStatus.BAD_REQUEST.value());
                return;
            }

            // 검증 후, 인증 객체를 생성하여 securityContextHolder에서 관리
            Claims memberInfo = jwtProvider.getMemberInfoFromToken(token);
            log.info("memberInfo.getSubject() : " + memberInfo.getSubject());
            setAuthentication(memberInfo.getSubject());         // subject = memberId
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String memberId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtProvider.createMemberAuthentication(memberId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String message, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");

        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalControllerAdvice().securityExceptionHandler());
            // ObjectMapper를 사용하여 SecurityExceptionDTO 객체를 JSON 문자열로 변환
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
