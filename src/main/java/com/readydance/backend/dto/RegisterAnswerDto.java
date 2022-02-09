package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterAnswerDto {

    @NotBlank(message = "질의응답 고유번호를 입력해주세요")
    private int qnaId;

    @NotBlank(message = "답변 내용을 입력해 주세요")
    private String content;
}
