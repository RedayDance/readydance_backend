package com.readydance.backend.dto;

import com.readydance.backend.entity.User;
import lombok.Getter;
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

@Getter
public class UserPrincipal implements  UserDetails {

    private static final long serialVersionUID = 1L;

    private int no;
    private String principal;   //유저 아이디
    private String password;    //유저 패스워드
    private String userEmail;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(int no, String password,String userEmail, Collection<? extends GrantedAuthority>  authorities, String principal ) {
        this.no = no;
        this.password = password;
        this.userEmail = userEmail;
        this.authorities = authorities;
        this.principal = principal;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getId(),
                user.getUsrPass(),
                user.getUsrEmail(),
                authorities,
                user.getUsrId()
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


    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    public String getPrincipal() {
        return principal;
    }
}
