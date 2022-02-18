package com.readydance.backend.listener;


import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.QandA;
import com.readydance.backend.entity.repository.FadRepository;
import com.readydance.backend.entity.repository.UserRepository;
import com.readydance.backend.util.BeanUtils;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.util.ArrayList;
import java.util.List;


public class FadEntityListener {

    @PostPersist
    @PostUpdate
    public void prePersistAndPreUpdate(Object o) {

        FadRepository fadRepository = BeanUtils.getBean(FadRepository.class);

        QandA qandA = (QandA) o;

        Fad fad = qandA.getFad();
        List<QandA> qandAList = fad.getQandAList();
        qandAList.add(qandA);
        fad.setQandAList(qandAList);
        fadRepository.save(fad);



    }
}
