package com.readydance.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 공통적으로 포함되는 entity 요소를 분리
 */
@Getter
@Setter
@MappedSuperclass //이 class를 상속을 하였으면 매핑을 해줘 아래 생성한 것들을 사용할 수 있다.
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @CreatedDate         //AuditingListener에서 현재 시간을 주입하여줌
    private LocalDateTime createdAt;

    @LastModifiedDate    //commit 직전에 현재 시간을 붙여줌
    private LocalDateTime updatedAt;

    public BaseEntity(int id) {
        this.id = id;
    }
}
