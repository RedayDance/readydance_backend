package com.readydance.backend.entity;

import com.readydance.backend.oauth2.AuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 유저 Entity(BaseEntity, UserDetails 상속)
 * BaseEntity : 공통적으로 포함되는 entity 요소를 분리
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity{

    @Column(name = "user_name", length = 45, nullable = false)
    private String username; //사용자 이름
    @Column(name = "user_pw", length = 70, nullable = false)
    private String password;  // hashed 비밀번호
    @Column(name = "user_email", length = 45, nullable = false, unique = true)
    private String email;
    @Column(name = "user_tel", length = 11, nullable = false, unique = true)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_sns_type", length = 45, nullable = false)
    private AuthProvider snsType; // 사용자 SNS 연동 타입 [local, naver, google, kakao]

    @Column(name = "user_sns_key", length = 45, unique = true)
    private String snsKey; // 사용자 SNS 고유 key

    @Builder
    public User(String username, String password, String email, String tel, AuthProvider snsType, String snsKey) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.snsType = snsType;
        this.snsKey = snsKey;
    }

    public static User of(User user) {
        return User.builder()
                .email(user.getEmail())
                .tel(user.getTel())
                .username(user.getUsername())
                .build();
    }


    public static List<User> listOf(List<User> users) {
        return users.stream().map(User::of)
                .collect(Collectors.toList());
    }
}
