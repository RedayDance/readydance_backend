package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.QandA;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FadRepository extends JpaRepository<Fad, Long> {

       public Optional<Fad> findById(int id);

       public List<Fad> findByFadNameContaining(String searchValue);


}
