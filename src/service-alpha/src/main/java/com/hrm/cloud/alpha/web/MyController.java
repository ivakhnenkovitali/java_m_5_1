package com.hrm.cloud.alpha.web;

import com.hrm.cloud.alpha.dto.MyDto;
import com.hrm.cloud.alpha.service.MyService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/alpha")
public class MyController {

    private final MyService myService;

    @PostMapping("/ping")
    @RateLimiter(name = "MyRateLimiter")
    public MyDto ping(@RequestBody MyDto myDto) {
        log.info("Controller invoked with param: {}", myDto);
        return myService.ping(myDto);
    }

}
