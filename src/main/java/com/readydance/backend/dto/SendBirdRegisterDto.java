package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendBirdRegisterDto {
    private String user_id;
    private String nickname;
    private String profile_url;

    public SendBirdRegisterDto(String user_id, String nickname, String profile_url) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.profile_url = profile_url;
    }
}
