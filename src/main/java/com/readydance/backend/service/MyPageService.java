package com.readydance.backend.service;


import com.readydance.backend.entity.Favorite;
import com.readydance.backend.entity.User;
import com.readydance.backend.entity.repository.FavoriteRepository;
import com.readydance.backend.entity.repository.UserRepository;
import com.readydance.backend.exception.SessionUnstableException;
import com.readydance.backend.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 회원별 즐겨찾기 저장목록 조회
     * @return
     */
    @Transactional
    public List<Favorite> getFavoriteList(String aToken) {
        String userEmail = JwtUtils.getUsername(aToken);
        User user = userRepository.findByUsrEmail(userEmail)
                .orElseThrow(() -> new SessionUnstableException("해당 유저가 존제하지 않습니다."));
        return user.getFavoriteList();
    }

    /**
     * 회원 프로필 조회
     * @return
     */
    @Transactional
    public User getUser(String aToken) {
        String userEmail = JwtUtils.getUsername(aToken);
        User user = userRepository.findByUsrEmail(userEmail)
                .orElseThrow(() -> new SessionUnstableException("해당 유저가 존제하지 않습니다."));

        return user;
    }
}
