package com.readydance.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoDto {
    private String origFileName;
    private String filePath;
    private long fileSize;
}
