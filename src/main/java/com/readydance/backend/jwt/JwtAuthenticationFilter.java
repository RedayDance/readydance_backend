package com.readydance.backend.jwt;

import com.readydance.backend.dto.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT를 이용한 로그인 인증
 * 로그인에 성공했을 시 User 정보로(payload 에 담아) JWT Token 을 생성하고 JWT 토큰을 응답 쿠키에 넣어준다.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    /**
     * 로그인 인증 시도
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        // 로그인할 때 입력한 username과 password를 가지고 authenticationToken를 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password"),
                new ArrayList<>()
        );
        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * 인증에 성공했을 때 사용
     * JWT Token 을 생성해서 쿠키에 넣는다.
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        UserPrincipal user = (UserPrincipal) authResult.getPrincipal();
        String token = JwtUtils.createToken(user);
        // 쿠키 생성(쿠키이름 + 값)
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, token);
        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정 (현재 10분으로 설정해 놓음)
        cookie.setPath("/"); //사용할 수 있는 경로 설정 (root)
        response.addCookie(cookie); //응답에 쿠키를 넣는다.
        response.sendRedirect("/"); //redirection url 을 root 로 설정
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException {
        response.sendRedirect("/login"); //실패 시 다시 login 페이지로 돌아갈 수 있도록 redirection 설정
    }
}