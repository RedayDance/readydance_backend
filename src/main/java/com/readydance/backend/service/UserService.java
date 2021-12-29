package com.readydance.backend.service;

import com.readydance.backend.dto.SendBirdRegisterDto;
import com.readydance.backend.dto.UserRegisterDto;
import com.readydance.backend.entity.User;
import com.readydance.backend.entity.repository.UserRepository;
import com.readydance.backend.exception.*;
import com.readydance.backend.oauth2.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 등록
     * @param username username
     * @param password password
     * @param email email
     * @param tel tel
     * @return 유저 권한을 가지고 있는 유저
     */
    public User signup(
            String username,
            String password,
            String email,
            String tel
    ) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyRegisteredUserException();
        }else{
            //샌드버드 회원가입
            URI uri = UriComponentsBuilder
                    .fromUriString("https://api-10F5977E-840D-4C3B-8FE1-E579E46DDC59.sendbird.com")
                    .path("/v3/users/")
                    .encode()       //Uri를 safe하게
                    .build()
                    .toUri();

            System.out.println(uri);

            //http body -> object -> object mapper -> json -> rest template -> http body json
            SendBirdRegisterDto req = new SendBirdRegisterDto(email,username,"");

            RequestEntity<SendBirdRegisterDto> requestEntity = RequestEntity
                    .post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Api-Token","6e3d41a864f6f761d8faa2b061380329afee4236")
                    .body(req);

            //서버가 어떤식으로 값을 줄지 모르겠을때는 String 으로 일단 찍어본다.
            RestTemplate restTemplate = new RestTemplate();
            //ResponseEntity<UserRegisterDto> response =
            restTemplate.exchange(requestEntity, UserRegisterDto.class);
            //ResponseEntity<UserRegisterDto> response = restTemplate.postForEntity(uri,req,SendBirdRegisterDto.class);
            return userRepository.save(new User(username, passwordEncoder.encode(password), email, tel , AuthProvider.local, ""));
        }
    }

    /**
     * 아이디, 비밀번호 올바른지 확인
     * @param email : 이메일 주소
     * @param password : 비밀번호
     * @return : 해당 유저 정보
     */
    public User checkLogIn(String email, String password) throws SessionUnstableException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SessionUnstableException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    /**
     * 로컬 회원 이메일 확인
     * @param email : 이메일 주소
     */
    public void checkDuplicateEmail(String email) throws DuplicateDataException {
        Optional<User> findUser = userRepository.findByEmail(email);

        if(findUser.isPresent()) {
            throw new DuplicateDataException("동일한 이메일의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 휴대폰 중복확인
     * @param phone_no : 휴대폰 번호
     */
    public void checkDuplicateTel(String phone_no) throws DuplicateDataException {
        Optional<User> findUser = userRepository.findByTel(phone_no);

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

    /**
     * 이메일로 회원 조회하기
     * @param email 조회할 회원의 이메일
     * @return : UserResponseDto를 반환
     */
    public User getProfile(String email) throws NotFoundDataException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundDataException("해당하는 회원을 찾을 수 없습니다."));
        return User.of(user);
    }

    /**
     * 회원 프로필 업데이트
     * @param username 회원의 이름
     * @param password 회원의 비밀번호
     * @param email 회원의 이메일
     * @param tel 회원의 전화번호
     * @return : UserResponseDto를 반환
     */
    public User updateProfile(String username, String password, String email, String tel) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundDataException("해당하는 회원을 찾을 수 없습니다."));
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setTel(tel);
        return userRepository.save(user);

    }

    /**
     * (개발용) 전체 회원 조회하기
     * @return List<UerResponseDto>를 반환
     */
    public List<User> showUsers() {
        List<User> users = userRepository.findAll();
        return User.listOf(users);
    }



}