package com.readydance.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.readydance.backend.listener.FadEntityListener;
import lombok.*;

import javax.persistence.*;

/**
 *즐겨찾기 정보 Entity
 */
@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fav")
public class Favorite   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 5, nullable = false, unique = true)
    private int id;         //즐겨찾기 고유 번호

    @Column(name = "FAV_TYPE", length = 1)
    private String favType; //즐겨찾기 유형

    @Column(name = "FAV_STATE", length = 1)
    private Boolean favState; //즐겨찾기 상태

    @Column(name = "FAV_RATE", length = 100)
    private String favRate; //즐겨찾기 별점

    @Column(name = "FAV_NAME", length = 50)
    private String favName; //즐겨찾기 이름

    @Column(name = "FAV_LOCATION", length = 100)
    private String favLocation; //즐겨찾기 지역
//
//    @Builder
//    public Favorite(String favType, Boolean favState, String favRate, String favName, String favLocation) {
//        this.favType = favType;
//        this.favState = favState;
//        this.favRate = favRate;
//        this.favName = favName;
//        this.favLocation = favLocation;
//    }

    @JsonBackReference
    @ManyToOne
    @ToString.Exclude
    private User user;

}
