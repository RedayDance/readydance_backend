package com.readydance.backend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.beans.ConstructorProperties;

@Getter
@Setter
public class SendMessageRequestDto {

    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    @NotBlank(message = "핸드폰 번호를 입력해 주세요")
    private String usrTel; // 문자를 전송할 핸드폰 번호

    private int validNo;

    @ConstructorProperties({"phoneNo","validNo"})
    public SendMessageRequestDto(String phoneNo, int validNo) {
        this.usrTel = phoneNo;
        this.validNo = validNo;
    }
}
