package com.readydance.backend.service;

import com.readydance.backend.dto.UserPrincipal;
import com.readydance.backend.entity.User;
import com.readydance.backend.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * Spring Security가 User 클래스를 사용해 Authentication을 사용
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usrId) throws UsernameNotFoundException {
        // User정보를 DB에서 가져온다
        User user = userRepository.findByUsrId(usrId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다."));
        return UserPrincipal.create(user);
    }
}