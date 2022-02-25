package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    public Optional<Favorite> findById(int id);
}
