package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.SecondaryTable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.beans.ConstructorProperties;

@Getter
@Setter
public class SendEmailRequestDto {

    @Email(message = "이메일 양식을 지켜주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String usrEmail; //수신 메일주소

    private String content;  //메일 내용

    @NotBlank(message = "이메일 제목을 입력해주세요.")
    private String title; //메일 제목

    @ConstructorProperties({"usrEmail","content","title"})
    public SendEmailRequestDto(String usrEmail, String content, String title) {
        this.usrEmail = usrEmail;
        this.content = content;
        this.title = title;
    }
}
