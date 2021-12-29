package com.readydance.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    String statusCode;
    String requestUrl;
    String resultCode;  //FAIL

    List<ErrorDetail> errorList;
}
