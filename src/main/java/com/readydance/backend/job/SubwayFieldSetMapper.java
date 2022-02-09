package com.readydance.backend.job;

import com.readydance.backend.entity.Subway;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class SubwayFieldSetMapper implements FieldSetMapper<Subway> {

    public static final String STATION_NAME ="stationName";
    public static final String X = "x";
    public static final String Y = "y";

    @Override
    public Subway mapFieldSet(FieldSet fieldSet) throws BindException {
        Subway subwayEntity = new Subway();
        subwayEntity.setStationName(fieldSet.readString(STATION_NAME));
        subwayEntity.setX(fieldSet.readString(X));
        subwayEntity.setY(fieldSet.readString(Y));
        return subwayEntity;
    }
}
