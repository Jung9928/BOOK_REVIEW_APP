package com.jung9407.bookreviewapp.configuration;

import com.jung9407.bookreviewapp.exception.jwt.CustomAuthenticationEntryPoint;
import com.jung9407.bookreviewapp.model.dao.RedisDAO;
import com.jung9407.bookreviewapp.model.dto.MemberDTO;
import com.jung9407.bookreviewapp.util.CookieUtils;
import com.jung9407.bookreviewapp.util.JwtProvider;
import com.jung9407.bookreviewapp.util.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final JwtProvider jwtProvider;

    private final RedisDAO redisDAO;

    private final CookieUtils cookieUtils;

    // 비번 암호화
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http
                .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));    // 프론트 서버 포트 접근 허용
                        configuration.setAllowedMethods(Collections.singletonList("*"));                        // GET/POST/PUT/DELETE 등 모든 메소드 허용
                        configuration.setAllowCredentials(true);                                                // 프론트 layer에서 Credential 설정을 헀다면 true로 설정 (쿠키 사용시 필요)
                        configuration.setAllowedHeaders(Collections.singletonList("*"));                        // 모든 헤더 허용
                        configuration.setMaxAge(3600L);                                                         // 허용 상태 유지 시간

                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));            // 백엔드 서버에서 프론트 쪽으로 헤더 전달 시, Authorization 헤더 허용.

                        return configuration;
                    }
                }));

        http
                .csrf((auth) -> auth.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하도록 설정
        http
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "/api/v1/members/login",
                                "/api/v1/members/signup",
                                "/api/v1/verifyEmail/**",
                                "/api/v1/members/verifyId/**",
                                "/api/v1/book/**",
                                "/"
                                ).permitAll()
                        .anyRequest().authenticated());


        // JWT 인증/인가를 사용하기 위한 설정
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, redisDAO, cookieUtils), UsernamePasswordAuthenticationFilter.class);

        // 401 Error 처리, Authorization(인증)과정에서 실패할 시 처리
        http
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }
}
