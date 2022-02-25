package com.readydance.backend.jwt;

import com.readydance.backend.dto.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

// Json 웹 토큰을 생성하고 확인
@Service
@Slf4j
public class JwtUtils {

    private Pair<String, Key> accessTokenSecret = JwtKey.getRandomKey();;

    private Pair<String, Key> refreshTokenSecret = JwtKey.getRandomKey();;


    private static final String HEADER_NAME = "Authorization";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public enum TokenType { ACCESS_TOKEN, REFRESH_TOKEN }

    /**
     * 토큰에서 username 찾기
     *
     * @param token 토큰
     * @return username
     */
    public static String getUsername(String token) {
        // parsing 을 통해 jwtToken 에서 username 을 찾는다.
        return Jwts.parserBuilder()
                //secret key 를 꺼내와 검증 실패 시 SignatureException 발생, 만료 시 ExpiredJwtException 발생
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)//토큰을 주입(파싱)
                .getBody()
                .getSubject(); // username
    }

    /**
     * user로 토큰 생성
     * HEADER(JWT 를 검증하는데 필요한 정보를 가진 객체) : alg(signature 에 사용한 암호화 알고리즘), kid
     * PAYLOAD(실질적으로 인증에 필요한 데이터를 저장) : sub(유저 아이디), iat(토큰 발행 시간), exp(토큰 만료 시간)
     * 인증할 떄 payload 에 있는 username 을 가져와서 조회할 때 사용한다.
     * SIGNATURE(jwt token 이 올바른지에 대한 일종의 서명) : JwtKey.getRandomKey 로 구한 Secret Key 로 HS512 해시(암호화)
     * signature 는 header 와 payload 를 합친 뒤 비밀키로 hash 를 생성하여 암호화 한다.
     * @param user 유저
     * @return jwt token
     */
    public static String createToken(UserPrincipal user) {
        Claims claims = Jwts.claims().setSubject(user.getUserEmail()); // subject
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();
        // JWT Token 생성
        return Jwts.builder()
                .setClaims(claims) // subject 정보 저장
                .setIssuedAt(now)  // 토큰 발행 시간 정보 저장
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME)) // 토큰 만료 시간 설정 저장
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // 헤더 설정 key.getFirst() : kid
                .signWith(key.getSecond()) // signature
                .compact();
    }

    // Access Token 발급
    public String generateAccessToken(UserPrincipal userPrincipal) {
        return createToken(userPrincipal);
    }

    // Refresh Token 발급
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        return createToken(userPrincipal);
    }

    // Request의 Header에서 token 파싱
    public String extractToken(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }

    // Jwt 토큰 유효성검사
    public boolean validateToken(String token) {
        Pair<String, Key> secretKey = accessTokenSecret;
        try {
            log.debug("validateToken's secretKey : " + secretKey);
            // 1. setSigningKey를 통해 디지털 서명되었는지를 확인한다.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey.getSecond())
                    .parseClaimsJws(token);
            // 2. 만료일자가 지났는지 확인한다.
            boolean isNotExpire = !claims.getBody().getExpiration().before(new Date()); // 만료되면 false를 반환
            // 3. 블랙리스트인지 확인한다.
            if (redisTemplate.opsForValue().get(token) != null) { // 블랙리스트에 access token이 존재할 경우
                log.info("이미 로그아웃 처리된 사용자입니다.");
                return false;
            }
            return isNotExpire;
        } catch (Exception e) {
            return false;
        }
    }

    public Date getExpirationDate(String token, TokenType tokenType) {
        Pair<String, Key> secretKey;

        if (tokenType == TokenType.ACCESS_TOKEN) {
            secretKey = accessTokenSecret;
        } else {
            secretKey = refreshTokenSecret;
        }

        log.debug("getExpirationDate's secretKey : " + secretKey);
        return Jwts.parser()
                .setSigningKey(secretKey.getSecond())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
