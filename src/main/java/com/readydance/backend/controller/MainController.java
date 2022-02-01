package com.readydance.backend.controller;

import com.readydance.backend.entity.MainPageRecData;
import com.readydance.backend.exception.DuplicateDataException;
import com.readydance.backend.exception.SMSException;
import com.readydance.backend.service.MainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"2. Get MainPage Data"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    @Autowired
    private final MainService mainService;

    /**
     * 모든 데이터 반환
     */
    @ApiOperation(value = "2데이터 반환", notes = "모든 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @GetMapping(value = "/GetMainInfo")
    public ResponseEntity<List<MainPageRecData>> getMainData()
            throws SMSException, DuplicateDataException {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageAllData());
    }

    /**
     * 2.1.1 학원 추천 데이터 반환
     */
    @ApiOperation(value = "2.1.1 학원 추천 데이터 반환", notes = "학원 추천 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @GetMapping(value = "/GetMainInfo/Academy")
    public ResponseEntity<List<MainPageRecData>> getMainAcademyData()
            throws SMSException, DuplicateDataException {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("A"));
    }

    /**
     * 2.1.2 연습실 추천 데이터 반환
     */
    @ApiOperation(value = "2.1.2 연습실 추천 반환", notes = "연습실 추천 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @GetMapping(value = "/GetMainInfo/PracticeRoom")
    public ResponseEntity<List<MainPageRecData>> getMainPracticeRoomData()
            throws SMSException, DuplicateDataException {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("P"));
    }

    /**
     * 2.1.3 댄서 추천 정보 반환
     */
    @ApiOperation(value = "2.1.3 댄서 추천 정보 반환", notes = "댄서 추천 정보 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @GetMapping(value = "/GetMainInfo/Dancer")
    public ResponseEntity<List<MainPageRecData>> getMainDancerData()
            throws SMSException, DuplicateDataException {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("D"));
    }

    /**
     * 2.1.4 원데이 클래스 추천 정보 반환
     */
    @ApiOperation(value = "2.1.4 원데이 클래스 추천 정보 반환", notes = "원데이 클래스 추천 정보 데이터를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @GetMapping(value = "/GetMainInfo/Class")
    public ResponseEntity<List<MainPageRecData>> getMainClassData()
            throws SMSException, DuplicateDataException {

        return ResponseEntity.status(HttpStatus.OK).body(mainService.getMainPageData("O"));
    }
}

