package com.readydance.backend.entity.repository;

import com.readydance.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    // email로 회원 찾기
    public Optional<User> findByEmail(String email);

    // 휴대폰번호로 회원 찾기
    public Optional<User> findByTel(String tel);
}