package com.hrm.cloud.bravo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BravoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BravoApplication.class, args);
    }

}
