package com.readydance.backend.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginReq {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String usrId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String usrPass;

    @NotBlank(message = "로그인 유형을 입력해주세요.")
    private String loginType;
}
