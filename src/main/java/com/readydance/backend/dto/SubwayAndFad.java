package com.readydance.backend.dto;

import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.Subway;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class SubwayAndFad {
//    private List<Subway> subways;
//    private List<Fad> fads;

    private String searchValue;
    private String searchType;
    private String x;
    private String y;

}
