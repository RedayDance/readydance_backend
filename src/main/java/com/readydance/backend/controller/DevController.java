package com.readydance.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.readydance.backend.dto.ResultDto;
import com.readydance.backend.dto.ResultListDto;
import com.readydance.backend.entity.Fad;
import com.readydance.backend.entity.QandA;
import com.readydance.backend.entity.User;
import com.readydance.backend.entity.repository.FadRepository;
import com.readydance.backend.entity.repository.QARepository;
import com.readydance.backend.entity.repository.UserRepository;
import com.readydance.backend.service.DevService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Dev Controller"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dev")
public class DevController {

    private final FadRepository fadRepository;
    private final UserRepository userRepository;
    private final QARepository qaRepository;
    private final DevService devService;
    ResultDto resultDto = new ResultDto();
    ResultListDto resultListDto = new ResultListDto();
    /**
     * 모든 시설 데이터 반환
     */
    @GetMapping(value = "/GetFad")
    public ResponseEntity<List<Fad>> getMainData() {
        return ResponseEntity.status(HttpStatus.OK).body(devService.getMainPageAllData());
    }

    /**
     * 데이터 반환 연습
     */
    @GetMapping(value = "/GetPracticeData")
    public ResultDto getPracticeData() {
        resultListDto.setCode(HttpStatus.OK.value());
        resultListDto.setData(devService.getMainPageAllData());
        resultDto.setMessage(HttpStatus.OK.toString());
        return resultDto;
    }

    /**
     * 모든 질문 데이터 반환
     */
    @GetMapping(value = "/GetQA")
    public String getQAData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate5Module());
        List<QandA> a =  devService.getQA();
        String str = mapper.writeValueAsString(a);

        return str;
    }

    @GetMapping(value = "/GetAllQa")
    public List<QandA> getAllQa() {
        return qaRepository.findAll();
    }

    /**
     * 모든 회원 데이터 반환
     * @return
     */
    @GetMapping(value = "/GetAllUser")
    public List<User> getAllUser() {
        System.out.println(userRepository.findById(1));
        System.out.println(fadRepository.findById(7));
        return userRepository.findAll();
    }
}
