package com.readydance.backend.controller;

import com.readydance.backend.dto.RegisterAnswerDto;
import com.readydance.backend.dto.RegisterQuestionDto;
import com.readydance.backend.dto.SendMessageRequestDto;
import com.readydance.backend.entity.MainPageRec;
import com.readydance.backend.entity.QandA;
import com.readydance.backend.entity.Subway;
import com.readydance.backend.service.MainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Api(tags = {"2. Get MainPage Data"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    @Autowired
    private final MainService mainService;

    //Todo Exception 추가

    /**
     * 모든 데이터 반환
     */
    @ApiOperation(value = "2데이터 반환", notes = "모든 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetMainInfo")
    public ResponseEntity<List<MainPageRec>> getMainData() {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageAllData());
    }

    /**
     * 2.1.1 학원 추천 데이터 반환
     */
    @ApiOperation(value = "2.1.1 학원 추천 데이터 반환", notes = "학원 추천 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetMainInfo/Academy")
    public ResponseEntity<List<MainPageRec>> getMainAcademyData() {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("A"));
    }

    /**
     * 2.1.2 연습실 추천 데이터 반환
     */
    @ApiOperation(value = "2.1.2 연습실 추천 반환", notes = "연습실 추천 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetMainInfo/PracticeRoom")
    public ResponseEntity<List<MainPageRec>> getMainPracticeRoomData() {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("P"));
    }

    /**
     * 2.1.3 댄서 추천 정보 반환
     */
    @ApiOperation(value = "2.1.3 댄서 추천 정보 반환", notes = "댄서 추천 정보 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetMainInfo/Dancer")
    public ResponseEntity<List<MainPageRec>> getMainDancerData() {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("D"));
    }

    /**
     * 2.1.4 원데이 클래스 추천 정보 반환
     */
    @ApiOperation(value = "2.1.4 원데이 클래스 추천 정보 반환", notes = "원데이 클래스 추천 정보 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetMainInfo/Class")
    public ResponseEntity<List<MainPageRec>> getMainClassData() {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("O"));
    }

    /**
     * 2. 지하철 정보 반환
     */
    @ApiOperation(value = "2. 지하철 정보 반환", notes = "지하철 정보 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/Subway")
    public ResponseEntity<List<Subway>> getSubwayData() {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getSubwayData());
    }

    /**
     * 2.4 문의하기
     */
    @ApiOperation(value = "2.4 문의하기", notes = "문의한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @PostMapping(value = "/SendAskMessage")
    public ResponseEntity<Void> sendAskMessage(SendMessageRequestDto sendMessageRequestDto) {

        //문자보내기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:7009")
                .path("/send-one")
                .encode()       //Uri를 safe하게
                .build()
                .toUri();

        System.out.println(uri);

        //http body -> object -> object mapper -> json -> rest template -> http body json
        SendMessageRequestDto req = new SendMessageRequestDto(sendMessageRequestDto.getSendingNumber(), sendMessageRequestDto.getReceiptNumber(), sendMessageRequestDto.getContent());

        RequestEntity<SendMessageRequestDto> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(req);

        //서버가 어떤식으로 값을 줄지 모르겠을때는 String 으로 일단 찍어본다.
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange(requestEntity, String.class);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 2.5 Q&A 반환
     */
    @ApiOperation(value = "2.5 Q&A 반환", notes = "질문 정보 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetQandA")
    public ResponseEntity<List<QandA>> getQandAData(@RequestParam int fadId) {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getQandAData(fadId));
    }

    /**
     * 2.6 질문 등록
     */
    @ApiOperation(value = "2.6 질문 등록", notes = "질문 내용을 등록한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @PostMapping(value = "/RegisterQuestion")
    public ResponseEntity<QandA> registerQuestion(@RequestBody RegisterQuestionDto registerQuestionDto) {

       return ResponseEntity.status(HttpStatus.OK).body(mainService.registerQuestion(registerQuestionDto.getUserId(),registerQuestionDto.getFadId(),registerQuestionDto.getContent()));

    }

    /**
     * 2.7 답변 등록
     */
    @ApiOperation(value = "2.7 답변 등록", notes = "답변 내용을 등록한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @PostMapping(value = "/RegisterAnswer")
    public ResponseEntity<QandA> registerAnswer(@RequestBody RegisterAnswerDto registerAnswerDto) {
        return ResponseEntity.status(HttpStatus.OK).body(mainService.registerAnswer(registerAnswerDto.getQnaId(),registerAnswerDto.getContent()));
    }

}

