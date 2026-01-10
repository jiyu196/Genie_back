package com.example.genie_tune_java.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Util {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public void upload (byte[] fileByte, String key, String contentType) {
    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(contentType)
            //.acl(ObjectCannedACL.PUBLIC_READ) //Access Control List 최신 버전은 허용하지 않음
            .build();

    s3Client.putObject(request, RequestBody.fromBytes(fileByte));
  }

}
