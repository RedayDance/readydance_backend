package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.QandA;
import com.readydance.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QARepository extends JpaRepository<QandA, Long> {

    public Optional<QandA> findById(int id);

}

