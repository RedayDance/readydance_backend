package com.readydance.backend.dto;


import com.readydance.backend.entity.User;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "image")

/**
 * 게시물 사진 Entity
 * 동일한 이름을 가진 파일이 업로드될 경우를 위해 원본명과 저장 경로를 따로 저장한다.
 */
public class Image {

    @Id
    @Column(length = 5, nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userPhoto;

    @Column(nullable = false)
    private String origFileName;  // 파일 원본명

    @Column(nullable = false)
    private String filePath;  // 파일 저장 경로

    private Long fileSize;    // 파일 사이즈

    @Builder
    public Image(String origFileName, String filePath, Long fileSize){
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

//    // Post 정보 저장
//    public void setPost(Post post){
//        this.post = post;
//
//        // 게시글에 현재 파일이 존재하지 않는다면
//        if(!post.getPostImage().contains(this))
//            // 파일 추가
//            post.getPostImage().add(this);
//    }
}
