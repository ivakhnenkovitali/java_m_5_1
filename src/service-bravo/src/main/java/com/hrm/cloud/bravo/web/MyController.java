package com.hrm.cloud.bravo.web;

import com.hrm.cloud.bravo.dto.MyDto;
import com.hrm.cloud.bravo.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/bravo")
public class MyController {

    private final MyService myService;

    @PostMapping("/ping")
    public MyDto ping(@RequestBody MyDto myDto) {
        log.info("Controller invoked with param: {}", myDto);
        return myService.ping(myDto);
    }

}
