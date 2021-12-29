package com.readydance.backend.dto;

import com.readydance.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 인증된 로그인 유저 정보
 * UserDetails : Spring Security 에서 상태 조회 및 인증과 인가를 할 때 사용
 */

public class UserPrincipal implements OAuth2User, UserDetails {

    private static final long serialVersionUID = 1L;

    private int no;
    private String principal;   //이메일
    private String password;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;


    public UserPrincipal(int no,String username, String password, Collection<? extends GrantedAuthority>  authorities, String principal ) {
        this.no = no;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.principal = principal;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.getEmail()
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return principal;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getPrincipal() {
        return principal;
    }
}
