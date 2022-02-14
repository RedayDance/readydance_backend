package com.readydance.backend.dto;

import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.Subway;
import lombok.Data;
import java.util.List;

@Data
public class SubwayAndFad {
    private List<Subway> subways;
    private List<Fad> fads;
}
