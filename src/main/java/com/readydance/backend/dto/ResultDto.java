package com.readydance.backend.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ResultDto {
    int code;
    HashMap<String,Object> data = new HashMap<String,Object>();
    String message;
}
