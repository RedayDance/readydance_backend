package com.readydance.backend.service;

import com.readydance.backend.entity.User;
import com.readydance.backend.entity.repository.UserRepository;
import com.readydance.backend.exception.DuplicateDataException;
import com.readydance.backend.exception.SMSException;
import com.readydance.backend.exception.SessionUnstableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 등록
     * @return 유저 권한을 가지고 있는 유저
     */
    @Transactional
    public List<User> saveUser(User user) throws DuplicateDataException {
            user.setUsrPass(passwordEncoder.encode(user.getUsrPass())); // 패스워드 인코딩을 써서 암호화한다.
            List<User> users = new ArrayList<>();
            users.add(userRepository.save(user));
            return users;
        }

    /**
     * 아이디, 비밀번호 올바른지 확인
     * @param usrId : 유저 아이디
     * @param usrPass : 유저 비밀번호
     * @return : 해당 유저 정보
     */
    public User checkLogIn(String usrId, String usrPass) throws SessionUnstableException {
        User user = userRepository.findByUsrId(usrId)
                .orElseThrow(() -> new SessionUnstableException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(usrPass, user.getUsrPass())) {
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return user;
    }
    /**
     * 휴대폰 중복확인
     * @param phone_no : 휴대폰 번호
     */
    public void checkDuplicateTel(String phone_no) throws DuplicateDataException {
        Optional<User> findUser = userRepository.findByUsrId(phone_no);

        if(findUser.isPresent()) {
            throw new DuplicateDataException("동일한 휴대폰 번호의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 본인확인 인증번호 핸드폰으로 전송
     * @param phone_no : 핸드폰 번호
     * @return : 생성된 5자리 인증번호
     */
    public int validatePhone(String phone_no) throws SMSException {
        int valiNum;

        // 첫자리가 0일 경우 나타나는 4자리 인증번호 방지
        do {
            valiNum = (int) (Math.random() * 100000);
        } while (valiNum < 10000);

        String message = " [인증번호]\n" + valiNum;
        // smsService.sendMessage(phone_no, message);
        return valiNum;
    }
}


