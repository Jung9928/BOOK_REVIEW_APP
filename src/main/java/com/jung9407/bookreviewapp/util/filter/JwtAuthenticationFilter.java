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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
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

    // 메인 페이지 (도서 검색 페이지) url은 jwt 검증 필터 작업 제외
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/api/v1/book",
                "/api/v1/login",
                "/api/v1/posts/**",
                "/api/v1/members/**",
                "/api/v1/verifyEmail/**",
                "/api/v1/comment/**"
        };

        // 제외할 url 설정
        String path = request.getRequestURI();
        log.info("path : " + path);
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("========= doFilterInternal 실행 시작 =========");

        // 1. Header에서 accessToken 가져오기
        String token = jwtProvider.getTokenFromHeader(request);
        log.info("token : " + token);

        // 2. accessToken 값이 존재하면
        if(token != null) {

            // 3. accessToken이 블랙리스트에 등록되었는지 확인 -> (로그아웃 한 경우)
            //    Redis에서 accessToken(key) / "logout"(value) 존재여부 확인.
            //    등록되어있다면 accessToken이 blacklist에 등록되어 있는 상태이고 만료된 토큰이므로 재 로그인 진행 안내.

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
            //    -> 즉, 유저가 로그아웃하지 않은 상태에서 access token 유효기간이 만료된 경우
            if(!jwtProvider.validateToken(token)) {
                response.sendError(401, "토큰이 만료되었습니다.");
//                jwtExceptionHandler(response, "401", HttpStatus.BAD_REQUEST.value());
                jwtExceptionHandler(response, "401", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 5. accessToken에 문제가 없다면 인증 객체를 생성하여 securityContextHolder에서 관리
            Claims memberInfo = jwtProvider.getMemberInfoFromToken(token);
            log.info("memberInfo.getSubject() : " + memberInfo.getSubject());
            setAuthentication(memberInfo.getSubject());         // subject = memberId
        }

        // 7. 유저가 로그아웃 or AccessToken이 만료되었을 경우 (클라이언트는 리프레시 토큰을 쿠키에 담아 서버에 요청)
        //    즉, reissue 요청일 경우 아래 작업 진행
        // 쿠키에서 refreshToken의 key값인 "Refresh" 가져오기
        Cookie refreshKeyFromCookie = cookieUtils.getCookie(request, "Refresh");
        log.info("refreshKeyFromCookie: " + refreshKeyFromCookie);

        // 클라이언트가 보낸 cookie에 refreshToken이 redis에 저장되어있는지 검증
        String refreshTokenFromCookie = "";
        if(refreshKeyFromCookie != null) {

            // 8. 쿠키에서 refreshToken 값 가져옴
            refreshTokenFromCookie = refreshKeyFromCookie.getValue();
            log.info("refreshToken from cookie: " + refreshTokenFromCookie);

            // 9. Redis에서 해당 유저의 id(key)로 refresh token(value) 값 가져옴
//            log.info("request.getParameter : " + request.getParameter("memberId"));
            Claims claimBody = jwtProvider.getMemberInfoFromToken(refreshTokenFromCookie);
            log.info("claimBody : " + claimBody);
//            String memberId = (String)jwtProvider.getMemberInfoFromToken(refreshTokenFromCookie).get("sub");
            String memberId = (String)claimBody.get("sub");
            log.info("memberId : " + memberId);
            String refreshTokenFromRedis = redisDAO.getRefreshToken(memberId);
            log.info("refreshToken from redis : " + refreshTokenFromRedis);

            // 10. redis에 refreshToken와 cookie의 refreshToken이 일치하지 않을 경우
            //     refresh token 만료 시간의 경우, redis에선 TTL이 만료되면 자동으로 해당 key를 삭제하므로
            //     key값이 존재한다면 TTL이 유효하다는 것을 의미 -> 만료시간 검증 로직 필요 X (단, @Indexed 사용 시에는 필요)
            if(!refreshTokenFromCookie.equals(refreshTokenFromRedis)) {
                response.sendError(401, "Cookie의 refreshToken과 Redis의 refresh토큰이 일치하지 않습니다.");
                jwtExceptionHandler(response, "401", HttpStatus.BAD_REQUEST.value());
                return;
            }

            // 11. refreshToken 검증에 문제가 없다면 인증 객체를 생성하여 securityContextHolder에서 관리
            Claims memberInfo = jwtProvider.getMemberInfoFromToken(refreshTokenFromRedis);
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
