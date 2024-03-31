package com.jung9407.bookreviewapp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jung9407.bookreviewapp.exception.ApplicationException;
import com.jung9407.bookreviewapp.exception.ErrorCode;
import com.jung9407.bookreviewapp.model.dao.RedisDAO;
import com.jung9407.bookreviewapp.model.dto.jwt.TokenResponseDTO;
import com.jung9407.bookreviewapp.service.CustomMemberDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final RedisDAO redisDAO;
    private final CustomMemberDetailsService customMemberDetailsService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.live.atk}")
    private long atkLive;

    @Value("${jwt.live.rtk}")
    private long rtkLive;


    // JWT signature에 들어갈 secret key 생성
    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Header에서 토큰 가져오기
     *
     * @param : request
     * @return
     * */
    public String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.info("bearerToken : " + bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 토큰으로 유저 정보 가져오기
     * @param : token
     * @return
     * */
    public Claims getMemberInfoFromToken(String token) {

        return Jwts.parserBuilder().setSigningKey(getKey(secretKey)).build().parseClaimsJws(token).getBody();
    }

    /**
     * 남은 액세스토큰의 만료시간 조회
     * @param : accessToken
     * @return
     * */
    public Long getExpirationTime(String accessToken) {
        // 액세스 토큰 만료시간
        Date expiration = Jwts.parserBuilder().setSigningKey(getKey(secretKey)).build().parseClaimsJws(accessToken).getBody().getExpiration();

        log.info("액세스 토큰 만료시간 : " + expiration);

        // 현재시간
        long now = new Date().getTime();

        log.info("현재 시간 : " + now);

        return (expiration.getTime() - now);
    }

    // Token 생성
    public static String generateToken(String memberId, MemberRole memberRole, String key, long expiredTimeMs) {

        //Claims claims = Jwts.claims().setSubject(jwtPayloadDtoToStr);
        //claims.put("memberId", jwtPayloadDTO.getMemberId());

        return Jwts.builder()
                .claim("USER", memberRole)                                        // JWT에 사용자 역할 정보를 claim에 추가.
                .setSubject(memberId)                                                   // JWT에 subject를 memberId로 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))                      // 토큰 발행일자
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))    // 토큰 유효기간
                .signWith(getKey(key), SignatureAlgorithm.HS256)                        // JWT에 서명(signature)추가 -> key는 서명에 사용되는 비밀키.
                .compact();
    }


    // 로그인 성공 시, 토큰 생성 및 발급 (Access Token, Refresh Token)
    public TokenResponseDTO generateTokenByLogin(String memberId, MemberRole memberRole, String key) {

        String accessToken = generateToken(memberId, memberRole, key, atkLive);          // access token 생성
        String refreshToken = generateToken(memberId, memberRole, key, rtkLive);          // refresh token 생성

        redisDAO.setRefreshToken(memberId, refreshToken, rtkLive);

        return new TokenResponseDTO(accessToken, refreshToken, memberId);
    }

    // JWT 필터의 RTK 검증단계에서 Cookie의 refreshToken 값과 Redis에 존재하는 refresh token 값이 같은지 확인 후, ATK 재발급 진행
    public TokenResponseDTO reissueAtk(String memberId, MemberRole memberRole, String reToken, HttpServletResponse response) {
        // 레디스에 저장된 리프레쉬 토큰 값을 가져와서 입력된 refreshToken과 같은 지 확인
        if(!redisDAO.getRefreshToken(memberId).equals(reToken)) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,
                    String.format("Cookie에서 가져온 Refresh토큰값과 Redis의 refrestoken값이 일치하지 않습니다.")
            );
        }

        String accessToken = generateToken(memberId, memberRole, secretKey, atkLive);
//        String refreshToken = generateToken(memberId, memberRole, secretKey, rtkLive);
//        redisDAO.setRefreshToken(memberId, refreshToken, rtkLive);
        log.info("reissueAtk 메소드 내에서 재발급한 accessToken : " + accessToken);

        response.addHeader("Authorization", accessToken);

        return new TokenResponseDTO(accessToken, reToken, memberId);
    }

    /**
     * 토큰 검증 메소드
     * @param : token
     * @return
     * */
    public boolean validateToken(String token) {
        try {
            // 주어진 토큰 파싱을 위해 JWT 파서를 설정하고 서명키를 설정한 뒤, 토큰 파싱하여 JWT 서명(signature)검사 수행.
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getKey(secretKey)).build().parseClaimsJws(token);
            log.info("claims : " + claims);
            return true;        // 토큰이 유효하면 true
        } catch (ExpiredJwtException e) {
            log.info("JWT Expired, 만료된 토큰입니다.");
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {   // 권한이 없다면
            log.info("Invalid JWT, 유효하지 않은 토큰 입니다.");
        } catch (IllegalArgumentException e) {                                              // JWT가 올바르게 구성되지 않았다면
            log.info("JWT claims is empty, 잘못된 토큰형식 입니다.");
        }

        return false;
    }


    /**
     * 일반 유저 인증 객체 생성
     * @param : memberId
     * @return
     * */
    public Authentication createMemberAuthentication(String memberId) {
        UserDetails userDetails = customMemberDetailsService.loadUserByUsername(memberId);
        return new UsernamePasswordAuthenticationToken(userDetails, null ,userDetails.getAuthorities());
    }
}
