package com.hrm.cloud.alpha.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("my.client.bravo")
public class BravoClientProperties {
    private String id;
    private String endpoint;
}
