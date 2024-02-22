package com.jung9407.bookreviewapp.configuration.filter;

import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.service.MemberService;
import com.jung9407.bookreviewapp.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * 1. 로그인 수행 -> 작성한 메소드에서 로그인 시, 결과 토큰 값을 리턴
         * 2. 리턴받은 토큰 결과 값을 http 프로토콜 header에 넣고
         *    필터에서 토큰 값을 읽어서 인증과정을 진행행
         *
         * 3. claims에 저장된 memberId를 가져와 여기서 실제 유저가 맞는지 유효성 검증 수행.
         *    */

        // 1. header에 있는 값 얻어오기
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);     // Authorization에 토큰 값을 넣을 것임.
        if(header == null || !header.startsWith("Bearer ")) {
            log.error("Header의 값을 가져오는데 오류가 발생했습니다. Header의 값이 null이거나 유효하지 않은지 확인바랍니다.");
            filterChain.doFilter(request, response);
            return;
       }

        // 헤더가 올바를 경우, Bearer을 제외하고 토큰값만 get
        try {
            final String token = header.split("")[1].trim();

            // 유효한 토큰인지 검증 수행
            if (JwtTokenUtils.isExpired(token, key)) {
                log.error("jwt 키가 만료되었습니다.");
                filterChain.doFilter(request, response);
                return;
            }


            // 토큰에서 memberId 추출
            String memberId = JwtTokenUtils.getUserId(token, key);

            // memberId 유효한지 검증
            MemberDTO memberDTO = memberService.getMemberByMemberId(memberId);

            // 모두 정상이면 controller에 결과 반환환
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    memberDTO, null, List.of(new SimpleGrantedAuthority(memberDTO.getMemberRole().toString()))
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (RuntimeException e) {
            log.error("토큰 검증 과정에서 오류가 발생했습니다. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
   }
}
