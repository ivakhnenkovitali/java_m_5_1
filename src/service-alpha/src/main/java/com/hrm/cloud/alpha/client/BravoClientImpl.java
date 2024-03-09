package com.hrm.cloud.alpha.client;

import com.hrm.cloud.alpha.config.BravoClientProperties;
import com.hrm.cloud.alpha.dto.MyDto;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class BravoClientImpl implements BravoClient {

    private final WebClient webClient;
    private final BravoClientProperties bravoClientConfig;
    private final LoadBalancerClient loadBalancerClient;

    @Override
    @Retry(name = "MyRetry")
    @Bulkhead(name = "MyBulkhead")
    public MyDto ping(MyDto myDto) {
        log.info("Sending request to BRAVO: {}", myDto);

        ServiceInstance instance = loadBalancerClient.choose(bravoClientConfig.getId());
        log.info("Choose instance {} of {} with LoadBalancerClient", instance.getInstanceId(), instance.getServiceId());

        MyDto response = webClient.post()
                .uri(uri -> uri.scheme(instance.getScheme())
                        .host(instance.getHost())
                        .port(instance.getPort())
                        .path(bravoClientConfig.getEndpoint())
                        .build())
                .bodyValue(myDto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(MyDto.class)
                .block();
        log.info("Received response from BRAVO: {}", response);
        return response;
    }
}
