package com.readydance.backend.service;

import com.readydance.backend.entity.*;
import com.readydance.backend.entity.repository.*;
import com.readydance.backend.exception.SessionUnstableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final MainRepository mainRepository;
    private final FadRepository fadRepository;
    private final UserRepository userRepository;
    private final QARepository qaRepository;
    private final SubwayRepository subwayRepository;

    /**
     * 메인 페이지 모든 데이터 반환
     * @return 모든 데이터 반환
     */
    @Transactional
    public List<MainPageRec> getMainPageAllData() {
        return mainRepository.findAll();
    }

    /**
     * 메인 페이지 타입별 데이터 반환
     * @return 선택한 타입 추천 데이터 반환
     */
    @Transactional
    public List<MainPageRec> getMainPageData(String type) {
        return mainRepository.findByPostType(type);
    }

    /**
     * 메인 페이지 타입별 데이터 반환
     * @return 선택한 타입 추천 데이터 반환
     */
    @Transactional
    public List<Subway> getSubwayData() {
        return subwayRepository.findAll();
    }

    /**
     * 학원별 전체 질문 내용 반환
     * @return 전체 질문 내용 반환
     */
    @Transactional
    public List<QandA> getQandAData(int fadNo) {
        Fad fad = fadRepository.findById(fadNo).orElseThrow(() -> new SessionUnstableException("해당 시설이 존재하지 않습니다."));
        return fad.getQandAList();
    }

    /**
     * 학원별 답변 내용 등록
     */
    @Transactional
    public QandA registerAnswer(int qnaNo, String content){
        QandA qandA = qaRepository.findById(qnaNo).orElseThrow(() -> new SessionUnstableException("해당 QA가 존재하지 않습니다."));
        qandA.setQnaA(content);
        return qaRepository.save(qandA);
    }

    /**
     * 학원별 질문 내용 등록
     */
    @Transactional
    public QandA registerQuestion(int userId, int fadId, String content) {
        return givenFadAndQandA(userId, fadId, content);
    }

    private QandA givenFadAndQandA(int userId, int fadId, String content){
       return givenReview(givenUser(userId), givenFad(fadId), content);
    }

    private User givenUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new SessionUnstableException("해당 유저가 존제하지 않습니다."));
    }

    private QandA givenReview(User user, Fad fad, String conent) {
        QandA qandA = new QandA();
        qandA.setQnaQ(conent);
        qandA.setUser(user);
        qandA.setFad(fad);

        return qaRepository.save(qandA);
    }

    private Fad givenFad(int fadId) {
        return fadRepository.findById(fadId).orElseThrow(() -> new SessionUnstableException("해당 시설이 존재하지 않습니다."));
    }

    /**
     * 질문 등록
     * @param memId : 회원 고유번호
     * @param content : 질문 내용
     * @return : 해당 유저 정보
     */
    @Transactional
    public List<QandA> getQandADat22a(int memId, String content) {
        return qaRepository.findAll();
    }

    /**
     * 답변 등록
     * @param memId : 회원 고유번호
     * @param content : 답변 내용
     * @return : 해당 유저 정보
     */
    @Transactional
    public List<QandA> getQandAD33ata(int memId, String content) {
        return qaRepository.findAll();
    }
}
