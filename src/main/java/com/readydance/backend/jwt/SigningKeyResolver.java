package com.readydance.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import java.security.Key;

/**
 * JwsHeader 를 통해 Signature 검증에 필요한 Key 를 가져오는 코드를 구현
 */
public class SigningKeyResolver extends SigningKeyResolverAdapter {
    public static SigningKeyResolver instance = new SigningKeyResolver();

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        //header 에서 kid 를 찾고 kid 로 secret key 를 가져옴
        //즉 secret key 를 꺼내와 검증을 할 수 있게 도와주는 역할
        String kid = jwsHeader.getKeyId();
        if (kid == null)
            return null;
        return JwtKey.getKey(kid);
    }
}