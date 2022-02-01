package com.readydance.backend.service;

import com.readydance.backend.entity.MainPageRecData;
import com.readydance.backend.entity.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final MainRepository mainRepository;

    /**
     * 메인 페이지 모든 데이터 반환
     * @return 모든 데이터 반환
     */
    @Transactional
    public List<MainPageRecData> getMainPageAllData() {
        return mainRepository.findAll();
    }

    /**
     * 메인 페이지 타입별 데이터 반환
     * @return 선택한 타입 추천 데이터 반환
     */
    @Transactional
    public List<MainPageRecData> getMainPageData(String type) {
        return mainRepository.findByPostType(type);
    }

}
