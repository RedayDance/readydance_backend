package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    public Optional<User> findByUsername(String username);
//
    // id로 회원 찾기
    public Optional<User> findByUsrId(String usrId);

    public Optional<User> findByUsrEmail(String userEmail);

    public Optional<User> findById(int id);

//    public Optional<User> findByUserId(Long id);

    // 휴대폰번호로 회원 찾기
    public Optional<User> findByUsrTel(String usrTel);


}