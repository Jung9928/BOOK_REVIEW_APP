package com.jung9407.bookreviewapp.model.dao;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisDAO {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * @param : memberId         (key)
     * @param : refreshToken     (리프레시 토큰) -> value
     * @param : refreshTokenTime (리프레시 토큰 유효시간)
     * */
    public void setRefreshToken(String memberId, String refreshToken, long refreshTokenTime) {
        // 리프레시 토큰 직렬화 -> 부수적인 데이터 압축효과 얻음
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));
        redisTemplate.opsForValue().set(memberId, refreshToken, refreshTokenTime, TimeUnit.SECONDS);

        // opsForValue() : RedisTemplate에서 ValueOperations를 가져옴
        // ValueOperations 객체를 통해 Redis의 값 저장, 조회, 삭제 작업 수행
    }

    /**
     * key로 값을 조회
     * @param : memberId (key)
     * @return 해당 리프레쉬 토큰
     * */
    public String getRefreshToken(String key) {
        log.info("redisTemplate.opsForValue().get(key) : " + redisTemplate.opsForValue().get(key));
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * key에 해당하는 refreshToken (value) 값을 삭제
     * @param : memberId (key)
     * */
    public void deleteRefreshToken(String memberId) {
        redisTemplate.delete(memberId);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 클라이언트에서 로그아웃 시도 시, 해당 유저의 accessToken을 key값으로 "logout"이라는 문자열을 value로 Redis에 저장
     * minutes : 로그아웃을 시도한 유저의 accessToken 유효시간
     * */
    public void setBlackList(String accessToken, String message, Long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(message.getClass()));
        redisTemplate.opsForValue().set(accessToken, message, minutes, TimeUnit.MINUTES);
    }

    public String getBlackList(String memberId) {
        return redisTemplate.opsForValue().get(memberId);
    }

    public boolean deleteBlackList(String memberId) {
        return Boolean.TRUE.equals(redisTemplate.delete(memberId));
    }

    /**
     * 레디스에 있는 모든 데이터를 삭제
     * */
    public void flushAll() {
        // serverCommands().flushAll()을 호출하여 Redis 서버의 flushAll() 명령을 실행하여 모든 데이터를 삭제.
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }
}
