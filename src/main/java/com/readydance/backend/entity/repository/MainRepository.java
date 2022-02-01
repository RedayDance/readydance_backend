package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.MainPageRecData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MainRepository extends JpaRepository<MainPageRecData, Long> {

    List<MainPageRecData> findByPostType(String postType);

}
