package com.hrm.cloud.alpha.service;

import com.hrm.cloud.alpha.client.BravoClient;
import com.hrm.cloud.alpha.client.StorageClient;
import com.hrm.cloud.alpha.dto.MyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyServiceImpl implements MyService {

    private final BravoClient bravoClient;
    private final StorageClient storageClient;

    @Override
    public MyDto ping(MyDto myDto) {
        log.info("Service invoked with param: {}", myDto);
        uploadToStorage(myDto);
        return bravoClient.ping(myDto);
    }

    private void uploadToStorage(MyDto myDto) {
        String randomKey = UUID.randomUUID().toString();
        byte[] contentToUpload = myDto.getMessage().getBytes();
        storageClient.upload(randomKey, contentToUpload);
    }
}
