package com.readydance.backend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ResultListDto {
    int code;
    List data = new ArrayList<>();
    String message;
}
