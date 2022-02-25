package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

@Getter
@Setter
public class AddFavoriteDto {

    @NotBlank(message = "액세스 토큰을 입력해주세요.")
    private String aToken;

    @NotNull(message = "시설 고유번호를 입력해주세요.")
    private int fadId;

    @ConstructorProperties({"aToken","fadId"})
    public AddFavoriteDto(String aToken, int fadId) {
        this.aToken = aToken;
        this.fadId = fadId;
    }
}
