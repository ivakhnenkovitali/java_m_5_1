package com.hrm.cloud.alpha.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final S3ClientProperties s3ClientProperties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(s3ClientProperties.getRegion()))
                .endpointOverride(URI.create(s3ClientProperties.getUrl()))
                .forcePathStyle(true)
                .httpClientBuilder(
                        ApacheHttpClient.builder()
                                .maxConnections(100)
                                .connectionTimeout(Duration.ofSeconds(5))
                )
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(s3ClientProperties.getAccessKey(), s3ClientProperties.getSecretKey())
                        )
                )
                .build();
    }

}
