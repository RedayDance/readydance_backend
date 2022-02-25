package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.Subway;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubwayRepository extends JpaRepository<Subway, Long> {

        List<Subway> findByStationNameContaining(String searchValue);



}