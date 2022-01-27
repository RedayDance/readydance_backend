package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

/**
 * 유저 회원가입용 Dto
 */
@Getter
@Setter
public class UserJoinDto {

    @NotBlank(message = "유저 타입을 입력해주세요.")
    private String usrType;  //유저 타입

    @NotBlank(message = "유저 아이디를 입력해주세요.")
    private String usrId;  //유저 아이디

    @NotBlank(message = "패스워드를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$", message = "영문,숫자,특수문자를 사용하여 8 ~ 15자리의 패스워드를 입력해주세요.")
    private String usrPass;  //유저 비밀번호

    @Email(message = "이메일 양식을 지켜주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String usrEmail;  //유저 이메일

    @NotBlank(message = "회원 이름을 입력해주세요.")
    private String usrName;  //유저 이름

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력해주세요.")
    private String usrTel;  //유저 핸드폰 번호

    private String usrImg; //유저 프로필 이미지
}
