package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.QandA;
import com.readydance.backend.entity.User;
import com.readydance.backend.exception.SessionUnstableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class FadRepositoryTest {
    @Autowired
    private FadRepository fadRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QARepository qaRepository;

    @Test
    @Transactional
    public void givenQandA() {
        givenFadAndQandA();
        Fad fad = givenFad();
        User user = userRepository.findByUsrId("suen99")
                .orElseThrow(() -> new SessionUnstableException("아이디 또는 비밀번호가 일치하지 않습니다."));

        System.out.println("QandA : " + user.getQandAList());
        System.out.println("Fad : " + user.getQandAList().get(0).getFad().getFadName());
        System.out.println("Fad Q : " + fad.getQandAList().get(0).getQnaQ());

    }

    private void givenFadAndQandA(){
        givenQA(givenUser(), givenFad());
    }

    private User givenUser() {
        return userRepository.findByUsrId("suen99")
                .orElseThrow(() -> new SessionUnstableException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

    private void givenQA (User user, Fad fad) {
        QandA qandA = new QandA();
        qandA.setQnaQ("주차 공간이 있을까요??");
        qandA.setQnaA("죄송하지만 주차공간은 따로 없습니다.");
        qandA.setUser(user);
        qandA.setFad(fad);

        qaRepository.save(qandA);
    }

    private Fad givenFad() {
        return fadRepository.findByFadName("분당댄스학원").get(0);
    }
}