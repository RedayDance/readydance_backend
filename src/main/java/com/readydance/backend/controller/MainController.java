package com.readydance.backend.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"2. Get MainPage Data"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
public class MainController {
}
