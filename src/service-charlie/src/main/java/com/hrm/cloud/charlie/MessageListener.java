package com.hrm.cloud.charlie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    @RabbitListener(queues = "my-queue")
    public void ping(String payload) {
        log.info("Received message with payload: {}", payload);
    }
}
