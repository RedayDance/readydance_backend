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
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
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
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * User 관련 Controller
 * */
@Api(tags = {"1. Managing User Authentication "})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenProvider;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private final JavaMailSender emailSender;

    ResultDto resultDto = new ResultDto();

    HashMap<String, Object> map = new HashMap<String, Object>();

    /**
     * 1.1 회원가입 api
     * 로컬 회원가입을 한다.
     * @return
     */
    @ApiOperation(value = "1-1 회원가입", notes = "로컬 회원가입을 한다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 회원가입이 완료되었습니다."),
            @ApiResponse(code = 403, message = "이미 등록된 유저입니다."),
            @ApiResponse(code = 412, message = "필수 항목이 누락되었습니다."),
            @ApiResponse(code = 500, message = "회원가입에 실패하였습니다.")
    })
    @PostMapping(value = "/Join")   //Todo throws 설정
    public ResultDto join(
            @RequestBody @Valid UserJoinDto userJoinDto
    ) throws DuplicateDataException {

        User user = User.builder()
                .usrType(userJoinDto.getUsrType())
                .usrId(userJoinDto.getUsrId())
                .usrPass(userJoinDto.getUsrPass())
                .usrEmail(userJoinDto.getUsrEmail())
                .usrTel(userJoinDto.getUsrTel())
                .usrName(userJoinDto.getUsrName())
                .usrImg(userJoinDto.getUsrImg())
                .build();

        resultDto.setCode(HttpStatus.OK.value());
        User registeredUser = userService.saveUser(user).get(0);

        map.put("ID", registeredUser.getId());
        map.put("USR_TYPE", registeredUser.getUsrType());
        map.put("USR_EMAIL", registeredUser.getUsrEmail());

        resultDto.setData(map);
        resultDto.setMessage(HttpStatus.OK.toString());
        return resultDto;
    }

    /**
     * 1.2 로컬 로그인 api
     * 로컬 로그인을 진행한다.
     * @return
     */
    @ApiOperation(value = "1-2 로컬 로그인", notes = "로컬 로그인을 진행한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 로그인 되었습니다.", response = AuthResponseDto.class),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "아이디 또는 비밀번호가 일치하지 않습니다."),
            @ApiResponse(code = 404, message = "등록된 회원이 아닙니다.."),
            @ApiResponse(code = 412, message = "필수 정보가 누락되었습니다."),
            @ApiResponse(code = 428, message = "비밀번호를 변경해야 합니다.", response = AuthResponseDto.class)
    })
    @PostMapping(value = "/Login")
    public ResultDto login(@RequestBody @Valid
                                                         LoginReq loginReq) throws SessionUnstableException {

        //계정 존재 여부 체크 후 객체 생성, 추후 임시 비밀번호 여부 체크 시 사용
        User user = userService.checkLogIn(loginReq.getUsrId(), loginReq.getUsrPass());

        //아이디와 패스워드로, Security 가 알아 볼 수 있는 token 객체로 변경한다.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginReq.getUsrId(), loginReq.getUsrPass());

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
//        redisTemplate.opsForValue().set(
//                userPrincipal.getPrincipal(), refreshToken,
//                expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
//        log.info("redis value : " + redisTemplate.opsForValue().get(userPrincipal.getPrincipal()));

        resultDto.setCode(HttpStatus.OK.value());
        map.put("USR_TYPE",user.getUsrType());
        map.put("ID",user.getId());
        map.put("USR_IMG",user.getUsrImg());
        map.put("A_TOKEN",new AuthResponseDto(accessToken,refreshToken).getAccessToken());
        map.put("R_TOKEN",new AuthResponseDto(accessToken,refreshToken).getRefreshToken());
        map.put("TOKEN_TYPE",new AuthResponseDto(accessToken,refreshToken).getTokenType());
        resultDto.setData(map);
        resultDto.setMessage(HttpStatus.OK.toString());

        return resultDto;
    }

    /**
     * 1.3 핸드폰 본인 인증 문자 송신
     */
    @ApiOperation(value = "1.3 핸드폰 본인 인증 문자 송신", notes = "핸드폰 본인 인증 문자를 송신한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @PostMapping(value = "/SendNumber")
    public ResponseEntity<Integer> validPhoneForSignUp(@RequestBody @Valid SendMessageRequestDto sendMessageRequestDto)
            throws SMSException, DuplicateDataException {

        userService.checkDuplicateTel(sendMessageRequestDto.getSendingNumber());
        int validNum = userService.validatePhone(sendMessageRequestDto.getSendingNumber());
        sendMessageRequestDto.setContent(Integer.toString(validNum));

        //문자보내기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7009")
                .path("/send-one")
                .encode()       //Uri를 safe하게
                .build()
                .toUri();

        System.out.println(uri);

        //http body -> object -> object mapper -> json -> rest template -> http body json
        SendMessageRequestDto req = new SendMessageRequestDto(
                sendMessageRequestDto.getSendingNumber(),
                sendMessageRequestDto.getReceiptNumber(),
                Integer.toString(validNum));

        RequestEntity<SendMessageRequestDto> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(req);

        //서버가 어떤식으로 값을 줄지 모르겠을때는 String 으로 일단 찍어본다.
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange(requestEntity, String.class);

        return ResponseEntity.status(HttpStatus.OK).body(validNum);
    }

    /**
     * 1.4 로그아웃
     */
    @ApiOperation(value = "1-4 로그아웃", notes = "로그인된 계정을 로그아웃 한다.", authorizations = {@Authorization(value = "jwtToken")})
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

    /**
     * 1.5 이메일 전송
     */
    @ApiOperation(value = "1.5 이메일 전송", notes = "미에일을 전송한다..")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "이메일 전송 실패")
    })
    @PostMapping(value = "/SendEmail")
    public ResponseEntity<Void> sendEmail (@RequestBody @Valid SendEmailRequestDto sendEmailRequestDto)
           {

        //@FunctionalInterface -> interface에 메소드가 하나 -> 람다식으로 구현 가능
        final MimeMessagePreparator preparator = message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(sendEmailRequestDto.getUsrEmail());
            helper.setSubject(sendEmailRequestDto.getTitle());
            helper.setText(sendEmailRequestDto.getContent());
        };

        emailSender.send(preparator);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
