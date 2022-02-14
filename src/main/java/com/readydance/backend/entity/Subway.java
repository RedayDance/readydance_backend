package com.readydance.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 지하철 정보 Entity
 */
@Getter
@Setter
@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "subway")
public class Subway {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 5, nullable = false, unique = true)
    private int id;

    @Column(name = "STATION_NAME", length = 20, nullable = false, unique = true)
    private String stationName;    //역이름

    @Column(name = "X", length = 30, nullable = false)
    private String x;      //위도

    @Column(name = "y", length = 30, nullable = false)
    private String y;      //경도

//    @ManyToOne
//    @ToString.Exclude
//    private Fad fad;
}
