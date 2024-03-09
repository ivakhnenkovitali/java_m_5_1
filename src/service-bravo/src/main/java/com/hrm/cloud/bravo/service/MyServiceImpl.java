package com.hrm.cloud.bravo.service;

import com.hrm.cloud.bravo.client.MessagePublisher;
import com.hrm.cloud.bravo.data.MyEntity;
import com.hrm.cloud.bravo.data.MyRepository;
import com.hrm.cloud.bravo.dto.MyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyServiceImpl implements MyService {

    private final MessagePublisher messagePublisher;
    private final MyRepository myRepository;
    private final MyMapper myMapper;

    @Override
    public MyDto ping(MyDto myDto) {
        log.info("Service invoked with param: {}", myDto);
        prepare(myDto);
        MyEntity myEntity = myMapper.toEntity(myDto);
        MyEntity saved = myRepository.save(myEntity);
        log.info("Service saved result: {}", saved);
        messagePublisher.postMessage("Saved with id: " + saved.getId());
        return myMapper.toDto(saved);
    }

    private void prepare(MyDto myDto) {
        myDto.setId(null);
    }
}
