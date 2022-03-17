package com.readydance.backend.dto;

import com.readydance.backend.entity.repository.FadRepository;
import lombok.Data;

import java.util.HashMap;

@Data
public class ResultData {

    int code;
    Object data = new Object();
    String message;
}
