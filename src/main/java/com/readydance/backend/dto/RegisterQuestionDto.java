package com.readydance.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterQuestionDto {

    @NotBlank(message = "회원 고유번호를 입력해주세요.")
    private int userId;

    @NotBlank(message = "검색정보 고유번호를 입력해주세요")
    private int fadId;

    @NotBlank(message = "질문 내용을 입력해 주세요")
    private String content;
}