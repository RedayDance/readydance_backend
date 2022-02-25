package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;

    private String tokenType = "Bearer";

    public AuthResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}