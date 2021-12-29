package com.readydance.backend.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

/**
 * JWT Key를 제공하고 조회
 */
public class JwtKey {
    /**
     * Kid-Key List 외부로 절대 유출되어서는 안되어 따라서 Key Rolling 적용
     * Key Rolling : Secret Key 를 여러개 사용하고 수시로 수정을 해주어서 안전한 상태로 유지
     * Secret Key 1개에 Unique Id (kid) 를 연결시켜 JWT 토큰을 만들 때 kid 를 포함하여 제공 하고 토큰을 해석할 때 kid 를 통해 signature 를 검증
     */
    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", "SpringSecurityJWTPracticeProjectIsSoGoodAndThisProjectIsSoFunSpringSecurityJWTPracticeProjectIsSoGoodAndThisProjectIsSoFun"
    );
    /*
     "key2", "GoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurityGoodSpringSecurityNiceSpringSecurity",
            "key3", "HelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurityHelloSpringSecurity"
     */
    //Todo 알고리즘 추가해서
    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static Random randomIndex = new Random();

    /**
     * 위 3가지 키 set 중에서 SECRET_KEY_SET 에서 랜덤한 KEY 를 가져온다.
     *
     * @return kid와 key Pair
     */
    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * kid 를 통해 Secret Key를 찾는다.
     *
     * @param kid kid
     * @return Key
     */
    public static Key getKey(String kid) {
        //SECRET_KEY 를 통해 객체 생성
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if (key == null)
            return null;
        //Secret_key 의 길이에 따라서 적절한 암호화 방식을 선택해줌
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}