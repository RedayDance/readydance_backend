package com.readydance.backend.controller;

import com.readydance.backend.dto.*;
import com.readydance.backend.entity.User;
import com.readydance.backend.exception.DuplicateDataException;
import com.readydance.backend.exception.InvalidTokenException;
import com.readydance.backend.exception.SMSException;
import com.readydance.backend.exception.SessionUnstableException;
import com.readydance.backend.jwt.JwtUtils;
import com.readydance.backend.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * User 관련 Controller
 */
@Api(tags = {"1. Managing User Authentication "})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenProvider;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 1.01 회원가입 api
     * SendBird 계정 및 로컬 계정을 생성한다.
     */
    @ApiOperation(value = "1-01 회원가입", notes = "SendBird 계정 및 로컬 계정을 생성한다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 회원가입이 완료되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 403, message = "이메일 또는 핸드폰번호가 중복되어 회원가입에 실패하였습니다."),
            @ApiResponse(code = 500, message = "이미 등록된 유저입니다.")
    })
    @PostMapping("/signup")
    public User signUp(
            @RequestBody @Valid UserRegisterDto userDto
    ) throws DuplicateDataException {
        return userService.signup(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getTel());
        //Todo 1.필요한 것만 return 하게 설정
    }

    /**
     * 1.02 로컬 로그인 api
     * 샌드버드 로그인 및 로컬 로그인을 진행한다.
     * 참고 blog : https://ziponia.github.io/2019/05/26/spring-security-authenticationmanager.html
     */
    @ApiOperation(value = "1-02 로컬 로그인", notes = "로컬 회원 로그인을 시도한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 로그인 되었습니다.", response = AuthResponseDto.class),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "아이디 또는 비밀번호가 일치하지 않습니다."),
            @ApiResponse(code = 428, message = "비밀번호를 변경해야 합니다.", response = AuthResponseDto.class)
    })
    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid
                                                         LoginReq loginReq) throws SessionUnstableException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://api-10F5977E-840D-4C3B-8FE1-E579E46DDC59.sendbird.com/v3/users/" + loginReq.getEmail())
//                .path("v3/users/")
//                .queryParam("????","loginReq.getEmail")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("Api-Token", "6e3d41a864f6f761d8faa2b061380329afee4236")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        // ResponseEntity<String> result =
        restTemplate.exchange(req, String.class);
//        if (result.getBody()==null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponseDto("BAD_REQUEST"));
//        }

        //계정 존재 여부 체크 후 객체 생성, 추후 임시 비밀번호 여부 체크 시 사용
        User user = userService.checkLogIn(loginReq.getEmail(), loginReq.getPassword());

        //아이디와 패스워드로, Security 가 알아 볼 수 있는 token 객체로 변경한다.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());

        //AuthenticationManager 에 token 을 넘기면 UserDetailsService 가 받아 처리하도록 한다.
        Authentication authentication = authenticationManager.authenticate(token);

        //실제 SecurityContext 에 authentication 정보를 등록한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Authentication 객체의 getPrincipal() 메서드를 실행하게 되면, UserDetails를 구현한 사용자 객체를 Return 한다.
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        /* access 토큰과 refresh 토큰을 발급 */
        String accessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userPrincipal);

        /* refresh 토큰을 redis에 저장 */
        Date expirationDate = jwtTokenProvider.getExpirationDate(refreshToken, JwtUtils.TokenType.REFRESH_TOKEN);
        redisTemplate.opsForValue().set(
                userPrincipal.getPrincipal(), refreshToken,
                expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
        log.info("redis value : " + redisTemplate.opsForValue().get(userPrincipal.getPrincipal()));

//        Todo /* 임시 비밀번호 여부 체크 */
//        if(user.isTempPassword()) {
//            return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(new AuthResponseDto(accessToken));
//        }

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(accessToken));
    }

    /**
     * 1.03 이메일 중복 확인
     */
    @ApiOperation(value = "1-03 이메일 중복확인", notes = "회원가입 이메일 입력시 중복확인")
    @ApiImplicitParam(name = "email", value = "중복확인을 진행할 이메일주소", required = true, dataType = "string", defaultValue = "example@google.com")
    @ApiResponses({
            @ApiResponse(code = 200, message = "이메일이 중복되지 않습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 403, message = "동일한 이메일의 회원이 이미 존재합니다.")
    })
    @GetMapping("/signup/emailcheck")
    public ResponseEntity<Void> validEmail(@RequestParam @Email(message = "이메일 양식을 지켜주세요.") String email) throws DuplicateDataException {
        userService.checkDuplicateEmail(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 1.04 회원가입시 핸드폰 본인인증
     */
    @ApiOperation(value = "1-04 회원가입시 핸드폰 본인인증", notes = "회원가입시 핸드폰 중복확인 후 본인인증 메세지를 전송한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 403, message = "동일한 휴대폰 번호의 회원이 이미 존재합니다."),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @PostMapping("signup/message")
    public ResponseEntity<Integer> validPhoneForSignUp(@ApiParam("휴대폰 번호") @Valid @RequestBody SendMessageRequestDto requestDto) throws SMSException, DuplicateDataException {
        userService.checkDuplicateTel(requestDto.getPhoneNo());
        int validNum = userService.validatePhone(requestDto.getPhoneNo());
        return ResponseEntity.status(HttpStatus.OK).body(validNum);
    }

    /**
     * 1.05 프로필 업데이트
     */
    @ApiOperation(value = "1-05 프로필 업데이트", notes = "회원의 프로필을 업데이트 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "프로필 업데이트 완료"),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 500, message = "프로필 업데이트 실패")
    })
    @PatchMapping("updateprofile")
    public ResponseEntity<Integer> updateProfile(@ApiParam("유저 정보") @Valid @RequestBody User user) throws RuntimeException {
        userService.updateProfile(user.getUsername(),user.getPassword(),user.getEmail(),user.getTel());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 1.06 프로필 조회
     */
    @ApiOperation(value = "1-05 프로필 업데이트", notes = "회원의 프로필을 업데이트 한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "프로필 업데이트 완료"),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 500, message = "프로필 업데이트 실패")
    })
    @GetMapping("profile")
    public ResponseEntity<Integer> getProfile(@ApiParam("유저 정보") @Valid @RequestBody User user) throws RuntimeException {
        userService.getProfile(user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 1.07 로그아웃
     */
    @ApiOperation(value = "1-07 로그아웃", notes = "로그인된 계정을 로그아웃 한다.", authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그아웃 되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                    "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
    })
    @PostMapping(value = "/me/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @ApiIgnore HttpServletRequest request) {
        String accessToken = jwtTokenProvider.extractToken(request);
        log.info("accessToken : " + accessToken);
        String principal = null;

        //* access token을 통해 userEmail or adminId를 찾아 redis에 저장된 refresh token을 삭제한다.*//
        try {
            principal = userPrincipal.getPrincipal();
        } catch (InvalidTokenException e) {
            log.error("userEmail or adminId가 유효한 토큰에 존재하지 않음.");
        }

        try {
            if (redisTemplate.opsForValue().get(principal) != null) {
                redisTemplate.delete(principal);
            }
        } catch (IllegalArgumentException e) {
            redisTemplate.delete(principal);
        }

        //* access token이 유효한 토큰인 경우 더 이상 사용하지 못하게 블랙리스트에 등록 *//
        if (jwtTokenProvider.validateToken(accessToken)) {
            Date expirationDate = jwtTokenProvider.getExpirationDate(accessToken, JwtUtils.TokenType.ACCESS_TOKEN);
            redisTemplate.opsForValue().set(
                    accessToken, true,
                    expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
            log.info("redis value : " + redisTemplate.opsForValue().get(accessToken));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

