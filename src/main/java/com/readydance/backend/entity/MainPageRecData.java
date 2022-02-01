package com.readydance.backend.entity;

import com.readydance.backend.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 메인페이지 추천 학원 정보를 담는 Class
 */
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Table(name = "main")
@Entity
public class MainPageRecData  {

    @Id
    @Column(name = "POST_NO", length = 5, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postNo;

    @XmlElement(name="POST_NAME")
    private String postName;

    @XmlElement(name="POST_PICTURE")
    private String postPicture;

    @XmlElement(name="POST_JANRE")
    private String postJanre;

    @XmlElement(name="POST_RATE")
    private String postRate;

    @XmlElement(name="POST_TYPE")
    private String postType;

    @XmlElement(name="POST_LOCATION")
    private String postLocation;

}
