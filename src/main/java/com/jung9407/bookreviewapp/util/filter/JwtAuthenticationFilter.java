package com.jung9407.bookreviewapp.util.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jung9407.bookreviewapp.exception.GlobalControllerAdvice;
import com.jung9407.bookreviewapp.model.dao.RedisDAO;
import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.util.CookieUtils;
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

/**
 * # 프로세스 개요 #
 *    OncePerRequestFilter를 상속하여 사용자의 요청을 가로채서 JWT 관련 작업 수행.
 * */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisDAO redisDAO;

    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("========= doFilterInternal 실행 시작 =========");

        String token = jwtProvider.getTokenFromHeader(request);
        log.info("token : " + token);

        // 1. Header에서 accessToken 가져오기
//        Cookie accessCookie = cookieUtils.getCookie(request, "Authorization");
//        log.info("accessCookie : " + accessCookie);

        // 2. accessToken 값이 존재하면
//        String token = "";
        if(token != null) {

            // 3. accessToken이 블랙리스트에 등록되었는지 확인 -> (로그아웃 한 경우)
            //    등록되어있다면 accessToken(key) / "logout"(value) 인지 확인.
            //    맞다면 accessToken이 blacklist에 등록되어 있는 상태이고 만료된 토큰이므로 재 로그인 진행 안내.
//            token = accessCookie.getValue();
//            log.info("cookie 안의 accessToken : " + token);

            String blackList = redisDAO.getBlackList(token);
            log.info("blackList : " + blackList);
            if(blackList != null) {
                if(blackList.equals("logout")) {
                    throw new IllegalArgumentException("다시 로그인을 진행해 주시기 바랍니다.");
                }
            }

            // 4. accessToken이 블랙리스트에 등록되어있지 않은경우
            //    -> 즉, 유저가 로그아웃하지 않은 상태에서 토큰 유효기간이 만료된 경우
            if(!jwtProvider.validateToken(token)) {
                response.sendError(401, "토큰이 만료되었습니다.");
                jwtExceptionHandler(response, "401", HttpStatus.BAD_REQUEST.value());
                return;
            }

            // 5. accessToken에 문제가 없다면 인증 객체를 생성하여 securityContextHolder에서 관리
            Claims memberInfo = jwtProvider.getMemberInfoFromToken(token);
            log.info("memberInfo.getSubject() : " + memberInfo.getSubject());
            setAuthentication(memberInfo.getSubject());         // subject = memberId
        }

        // 7. 유저가 로그아웃 or AccessToken이 만료되었을 경우 (클라이언트는 atk/rtk를 쿠키에 담아 서버에 요청)
        //    즉, reissue 요청일 경우 아래 작업 진행
        // 쿠키에서 refreshToken 가져오기
        Cookie refreshCookie = cookieUtils.getCookie(request, "refresh");
        log.info("refreshCookie: " + refreshCookie);

        // 클라이언트가 보낸 cookie에 refreshToken이 redis에 저장되어있는지 검증
        // 저장되어 있다면 refreshToken의 유효시간을 검증
        String refreshTokenFromCookie = "";
        if(refreshCookie != null) {

            // 8. 쿠키에서 가져온 refreshToken이 redis에 존재하는지 검증
            //    사용자의 id값(key)로 redis에서 refreshToken(value)가져온 후, 비교
            refreshTokenFromCookie = refreshCookie.getValue();
            log.info("refreshToken from cookie: " + refreshTokenFromCookie);

            String refreshTokenFromRedis = redisDAO.getRefreshToken(request.getParameter("memberId"));

            log.info("refreshToken from redis : " + refreshTokenFromRedis);

            // 9. redis에 refreshToken와 cookie의 refreshToken이 일치하지 않을 경우
            if(!refreshTokenFromCookie.equals(refreshTokenFromRedis)) {
                response.sendError(401, "Cookie의 refreshToken과 Redis의 refresh토큰이 일치하지 않습니다.");
                jwtExceptionHandler(response, "401", HttpStatus.BAD_REQUEST.value());
                return;
            }

            // 10. refreshToken 검증에 문제가 없다면 인증 객체를 생성하여 securityContextHolder에서 관리
            Claims memberInfo = jwtProvider.getMemberInfoFromToken(token);
            log.info("memberInfo.getSubject() : " + memberInfo.getSubject());
            setAuthentication(memberInfo.getSubject());         // subject = memberId
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String memberId) {
        log.info("setAuthentication 수행");
        log.info("setAuthentication memberId = " + memberId);
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
