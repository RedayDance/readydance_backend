package com.readydance.backend.config;

import com.readydance.backend.jwt.JwtAuthenticationFilter;
import com.readydance.backend.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 설정 Config
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // basic authentication filter 비활성화
        http.httpBasic().disable();
        // basic authentication filter 비활성화
        http.csrf().disable();
        // remember-me (기록 우선 제거)
        http.rememberMe().disable();
        // stateless 세션을 사용하지 않는다.(우리는 jwt 를 이용하므로)
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // jwt filter 등록
        http.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        );
    }

    @Override
    public void configure(WebSecurity web) {
        // 정적 리소스 spring security 대상에서 제외 (예 : css 파일 또는 png 파일)
        // web.ignoring().antMatchers("/images/**", "/css/**"); 아래 코드와 같은 코드
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }

    // Authorization에 사용할 userDetailService와 password Encoder를 정의한다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoderConfig passwordEncoderConfig = new PasswordEncoderConfig();
        auth.userDetailsService(customUserDetailsService).passwordEncoder((passwordEncoderConfig.passwordEncoder()));
    }

    /*
     * 다른 AuthorizationServer나 ResourceServer가 참조할 수 있도록 오버라이딩 해서 빈으로 등록
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
