package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.MainPageRec;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MainRepository extends JpaRepository<MainPageRec, Long> {

    List<MainPageRec> findByPostType(String postType);

}
