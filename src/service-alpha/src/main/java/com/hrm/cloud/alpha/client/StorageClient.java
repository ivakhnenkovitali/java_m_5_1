package com.hrm.cloud.alpha.client;

public interface StorageClient {

    void upload(String key, byte[] content);

    byte[] download(String key);

    void delete(String key);

}
