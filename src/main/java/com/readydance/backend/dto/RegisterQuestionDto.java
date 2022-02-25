package com.readydance.backend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

@Getter
@Setter
public class RegisterQuestionDto {

    @NotBlank(message = "액세스 토큰을 입력해주세요.")
    private String aToken;

    @NotNull(message = "검색정보 고유번호를 입력해주세요")
    private int fadId;

    @NotBlank(message = "질문 내용을 입력해 주세요")
    private String content;

    @ConstructorProperties({"aToken","fadId","content"})
    public RegisterQuestionDto(String aToken, int fadId, String content) {
        this.aToken = aToken;
        this.fadId = fadId;
        this.content = content;
    }
}