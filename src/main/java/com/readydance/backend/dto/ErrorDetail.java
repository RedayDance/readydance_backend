package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetail {
    private String field;   //에러가 난 field ex : password, email
    private String message; //error message
    private String invalidValue; //입력한 value
}
