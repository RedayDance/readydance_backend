package com.readydance.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.readydance.backend.listener.FadEntityListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Set;

/**
 * Q&A 정보 Entity
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "qa")
@EntityListeners(value = { FadEntityListener.class })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class QandA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 5, nullable = false, unique = true)
    private int id;      //질의응답 고유 번호

    @Column(name = "QNA_Q", length = 1000, nullable = false)
    private String qnaQ;    //질문 내용

    @Column(name = "QNA_A", length = 1000)
    private String qnaA;    //답변 내용

    @ManyToOne
    @ToString.Exclude
    private Fad fad;

    @ManyToOne
    @ToString.Exclude
    private User user;
}
