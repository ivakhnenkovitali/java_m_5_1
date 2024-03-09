package com.hrm.cloud.alpha.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class StorageClientImpl implements StorageClient {

    @Value("${my.client.s3.bucket}")
    private String bucket;
    private final S3Client s3Client;


    @Override
    @CircuitBreaker(name = "MyCircuitBreaker", fallbackMethod = "fallbackUpload")
    public void upload(String key, byte[] content) {
        if (!bucketExists(bucket)) {
            createBucket(bucket);
        }
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
        s3Client.putObject(request, RequestBody.fromBytes(content));
        log.info("Uploaded to S3: {}, length: {}", key, content.length);
    }

    private void fallbackUpload(String key, byte[] content, Throwable e) {
        log.info("Fallback upload method called with arguments: key={}, content length={}, and caught throwable: {}",
                key, content.length, e.getMessage());
    }

    @Override
    @SneakyThrows(IOException.class)
    public byte[] download(String key) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucket).key(key).build();
        byte[] content = s3Client.getObject(request).readAllBytes();
        log.info("Downloaded from S3: {}, length: {}", key, content.length);
        return content;
    }

    @Override
    public void delete(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
        s3Client.deleteObject(request);
        log.info("Deleted from S3: '{}'", key);
    }

    private boolean bucketExists(String bucketName) {
        HeadBucketRequest request = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();
        try {
            s3Client.headBucket(request);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }

    }

    private void createBucket(String bucketName) {
        log.info("Creating S3 bucket: '{}'", bucketName);
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        s3Client.createBucket(bucketRequest);

        S3Waiter s3Waiter = s3Client.waiter();
        HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

        WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
        waiterResponse.matched().response().map(Object::toString).ifPresent(log::info);
        log.info("Created S3 bucket: '{}'", bucketName);
    }
}
