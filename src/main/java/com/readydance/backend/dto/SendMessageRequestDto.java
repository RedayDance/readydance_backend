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
    private String sendingNumber; // 문자를 송신할 핸드폰 번호

    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    @NotBlank(message = "핸드폰 번호를 입력해 주세요")
    private String receiptNumber; // 문자를 수신할 핸드폰 번호

    private String content; // 문자 내용


    @ConstructorProperties({"sendingNumber","receiptNumber", "content"})
    public SendMessageRequestDto(String sendingNumber, String receiptNumber, String content) {
        this.sendingNumber = sendingNumber;
        this.receiptNumber = receiptNumber;
        this.content = content;

    }
}
