package com.readydance.backend.controller;

import com.readydance.backend.dto.*;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
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
    @ApiOperation(value = "데이터 반환", notes = "모든 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetMainInfo")
    public ResultListDto getMainData() {
        ResultListDto resultListDto = new ResultListDto();
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(mainService.getMainPageAllData());
        return resultListDto;
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
    public ResultListDto getMainAcademyData() {
        ResultListDto resultListDto = new ResultListDto();
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(mainService.getMainPageData("A"));
        return resultListDto;
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
    public ResultListDto getMainPracticeRoomData() {
        ResultListDto resultListDto = new ResultListDto();
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(mainService.getMainPageData("P"));
        return resultListDto;
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
    public ResultListDto getMainDancerData() {
        ResultListDto resultListDto = new ResultListDto();
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(mainService.getMainPageData("D"));
        return resultListDto;
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
    public ResultListDto getMainClassData() {
        ResultListDto resultListDto = new ResultListDto();
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(mainService.getMainPageData("O"));
        return resultListDto;
    }

    /**
     * 2.2 키워드 검색(메인)
     */
    @ApiOperation(value = "2.2 키워드 검색", notes = "지역, 지하철역, 시설, 댄서 키워드를 통해 검색한 결과값을 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/SearchKeywords")
    public ResultListDto searchKeywords(@RequestParam String searchValue) {
        ResultListDto resultListDto = new ResultListDto();
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(mainService.getSearchData(searchValue));
        return resultListDto;
    }

    /**
     * 2.3 상세페이지 검색
     */
    @ApiOperation(value = "2.3 상세페이지 검색", notes = "지역, 지하철역, 시설, 댄서 키워드를 통해 검색한 결과값을 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/SearchDetailed")
    public ResultData searchDetail(@RequestParam int fadId) {
        ResultData resultData = new ResultData();
        resultData.setCode(HttpStatus.OK.value());
        resultData.setMessage(HttpStatus.OK.toString());
        resultData.setData(mainService.getDetailData(fadId));
        return resultData;
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
    public ResultData sendAskMessage(@RequestBody @Valid SendMessageRequestDto sendMessageRequestDto) {

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
        ResultData resultData = new ResultData();
        resultData.setData(null);
        resultData.setCode(HttpStatus.OK.value());
        resultData.setMessage(HttpStatus.OK.toString());

        return resultData;
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
    public ResultListDto getQandAData(@RequestParam int fadId) {
        ResultListDto resultListDto = new ResultListDto();
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(mainService.getQandAData(fadId));
        return resultListDto;
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
    public ResultData registerQuestion(@RequestBody @Valid RegisterQuestionDto registerQuestionDto) {
        ResultData resultData = new ResultData();
       resultData.setCode(HttpStatus.OK.value());
       resultData.setMessage(HttpStatus.OK.toString());
       resultData.setData(mainService.registerQuestion(registerQuestionDto.getAToken(),registerQuestionDto.getFadId(),registerQuestionDto.getContent()));
       return resultData;
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
    public ResultData registerAnswer(@RequestBody RegisterAnswerDto registerAnswerDto) {
        ResultData resultData = new ResultData();
        resultData.setCode(HttpStatus.OK.value());
        resultData.setMessage(HttpStatus.OK.toString());
        resultData.setData(mainService.registerAnswer(registerAnswerDto.getQnaId(),registerAnswerDto.getContent()));
        return resultData;
    }

    /**
     * 2.8 즐겨찾기 추가
     */
    @ApiOperation(value = "2.8 즐겨찾기 추가", notes = "회원의 즐겨찾기에 목록에 추가한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @PostMapping(value = "/AddFavorite")
    public ResultData addFavorite(@RequestBody @Valid AddFavoriteDto addFavoriteDto) {
        ResultData resultData = new ResultData();
        resultData.setCode(HttpStatus.OK.value());
        resultData.setMessage(HttpStatus.OK.toString());
        resultData.setData(mainService.addFavorite(addFavoriteDto.getAToken(),addFavoriteDto.getFadId()));
        return resultData;
    }

    /**
     * 2.9 즐겨찾기 제거
     */
    @ApiOperation(value = "2.9 즐겨찾기 제거", notes = "회원의 즐겨찾기에 목록에 추가한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @PostMapping(value = "/DeleteFavorite")
    public ResultData deleteFavorite(@RequestBody @Valid DeleteFavoriteDto deleteFavoriteDto) {

        ResultData resultData = new ResultData();
        resultData.setCode(HttpStatus.OK.value());
        resultData.setMessage(HttpStatus.OK.toString());
        resultData.setData(mainService.deleteFavorite(deleteFavoriteDto.getFavId()));

        return resultData;
    }
}