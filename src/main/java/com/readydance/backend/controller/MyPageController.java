package com.readydance.backend.controller;

import com.readydance.backend.dto.ResultDto;
import com.readydance.backend.dto.ResultListDto;
import com.readydance.backend.entity.User;
import com.readydance.backend.service.MyPageService;
import com.readydance.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Api(tags = {"3. Get MyPage Data"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my")
public class MyPageController {

    private final MyPageService myPageService;


    /**
     * 3.1 즐겨찾기 저장목록 조회
     */
    @ApiOperation(value = "3.1 즐겨찾기 저장목록 조회", notes = "회원이 저장한 저장 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetFavList")
    public ResultListDto getFavList(@RequestParam String aToken) {
        ResultListDto resultListDto = new ResultListDto();

        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setMessage(HttpStatus.OK.toString());
        resultListDto.setData(myPageService.getFavoriteList(aToken));
        return resultListDto;
    }

    /**
     * 3.2 프로필 조회
     */
    @ApiOperation(value = "3.2 프로필 조회", notes = "회원의 프로필을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 412, message = "필수항목 누락"),
            @ApiResponse(code = 500, message = "실패")
    })
    @GetMapping(value = "/GetProfile")
    public ResultDto getProfile(@RequestParam String aToken) {
        ResultDto resultDto = new ResultDto();
        HashMap<String, Object> map = new HashMap<String, Object>();
        User user = myPageService.getUser(aToken);
        map.put("usrId",user.getUsrId());
        map.put("usrName",user.getUsrName());
        map.put("usrTel",user.getUsrTel());
        map.put("usrEmail",user.getUsrEmail());
//        map.put("usrPass",user.getUsrPass());
        resultDto.setCode(HttpStatus.OK.value());
        resultDto.setMessage(HttpStatus.OK.toString());
        resultDto.setData(map);
        return resultDto;
    }
}
