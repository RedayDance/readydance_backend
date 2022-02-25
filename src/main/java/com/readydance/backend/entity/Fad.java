package com.readydance.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * 검색용 시설 및 댄서 Entity(Facility, Dancer)
 */
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "fad")
public class Fad {

    @Id
    @Column(length = 5, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "fad_id")
    @ToString.Exclude
    private List<QandA> qandAList = new ArrayList<>();

    @XmlElement(name="FAD_TYPE")
    private String fadType;

    @XmlElement(name="FAD_IMG")
    private String fadImg;

    @XmlElement(name="FAD_TEL")
    private String fadTel;

    @XmlElement(name="FAD_NAME")
    private String fadName;

    @XmlElement(name="FAD_X")
    private String fadX;

    @XmlElement(name="FAD_Y")
    private String fadY;

    @XmlElement(name="FAD_RATE")
    private String fadRate;

    @XmlElement(name="FAD_JANRE")
    private String fadJanre;

    @XmlElement(name="FAD_DLO")
    private String fadDlo;

    @XmlElement(name="FAD_BNAME")
    private String fadBName;

    @XmlElement(name="FAD_CAREER")
    private String fadCareer;

    @XmlElement(name="FAD_FEE")
    private String fadFee;

    @XmlElement(name="FAD_PR")
    private String fadPr;

    @XmlElement(name="FAD_AREA")
    private String fadArea;

    @XmlElement(name="FAD_BDANCER")
    private String fadBDancer;

    @XmlElement(name="FAD_URL")
    private String fadUrl;

    @XmlElement(name="FAD_INT")
    private String fadInt;

    @XmlElement(name="FAD_INFO")
    private String fadInfo;

    @XmlElement(name="FAD_PRICE")
    private String fadPrice;

    @XmlElement(name="FAD_CAU")
    private String fadCau;

}
