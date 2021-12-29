package com.readydance.backend.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginReq {
    @Email(message = "이메일 양식을 지켜주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;
}
