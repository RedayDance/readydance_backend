package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

@Getter
@Setter
public class DeleteFavoriteDto {
    @NotBlank(message = "액세스 토큰을 입력해주세요.")
    private String aToken;

    @NotNull(message = "즐겨찾기 고유번호를 입력해주세요.")
    private int favId;

    @ConstructorProperties({"aToken", "favId"})
    public DeleteFavoriteDto(String aToken, int favId) {
        this.aToken = aToken;
        this.favId = favId;
    }
}