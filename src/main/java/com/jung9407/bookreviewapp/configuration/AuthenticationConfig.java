package com.jung9407.bookreviewapp.configuration;

import com.jung9407.bookreviewapp.configuration.filter.JwtTokenFilter;
import com.jung9407.bookreviewapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity    // 웹 보안 활성화를 위한 어노테이션
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final MemberService memberService;

    @Value("${jwt.secret-key}")
    private String key;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/*/members/signup", "/api/*/members/login").permitAll()
                        .requestMatchers("/api/**").authenticated()

                )
                .addFilterBefore(new JwtTokenFilter(key, memberService), UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }
}

