package com.readydance.backend.controller;

import com.readydance.backend.dto.AuthResponseDto;
import com.readydance.backend.entity.User;
import com.readydance.backend.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 개발용 컨트롤러
 */
@RestController
@Slf4j
@RequestMapping("/dev")
public class DevController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "(개발용) 회원 전체 조회", notes = "회원목록을 조회한다", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원목록을 조회하는데 성공했습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                    "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
    })
    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> showUsers() {
        List<User> userResponseDtos = userService.showUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtos);
    }

//    @ApiOperation(value = "(개발용) 이메일로 회원 조회", notes = "이메일로 회원을 조회한다.", authorizations = { @Authorization(value = "jwtToken")})
//    @ApiImplicitParam(name = "email", value = "조회할 회원의 이메일주소", required = true, dataType = "string", defaultValue = "example@google.com")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "정상적으로 해당 회원정보가 조회되었습니다."),
//            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
//            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
//                    "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
//            @ApiResponse(code = 403, message = "관리자만 접근 가능"),
//            @ApiResponse(code = 404, message = "일치하는 회원이 존재하지 않습니다.")
//    })
//    @GetMapping("")
//    public ResponseEntity<User> showUser(@RequestParam @Email(message = "이메일 양식을 지켜주세요") String email) throws NotFoundDataException {
//        User userResponseDto = userService.showUser(email);
//        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
//    }

    @GetMapping("/getId")
    public String SendbirdId(){
        URI uri = UriComponentsBuilder
                .fromUriString("https://api-10F5977E-840D-4C3B-8FE1-E579E46DDC59.sendbird.com/v3/users/"+"suen77@naver.com")
//                .path("v3/users/")
//                .queryParam("????","suen66")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("Api-Token", "6e3d41a864f6f761d8faa2b061380329afee4236")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        return result.getBody();
    }



}
