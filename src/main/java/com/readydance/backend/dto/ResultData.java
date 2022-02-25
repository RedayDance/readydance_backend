package com.readydance.backend.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class ResultData {
    int code;
    Object data = new Object();
    String message;
}
