package com.example.aliyundemo.service;

import com.example.aliyundemo.model.FileMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.provider", havingValue = "aws")
public class AwsStorageService implements StorageService {
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket}")
    private String bucketName;
    
    @Value("${aws.region}")
    private String region;
    
    @Override
    public FileMetadata uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String key = UUID.randomUUID().toString() + "_" + fileName;
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(file.getContentType())
            .build();
        
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(fileName);
        metadata.setOssKey(key);
        metadata.setContentType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setUploadTime(LocalDateTime.now());
        metadata.setUrl(generateUrl(key));
        
        return metadata;
    }
    
    @Override
    public byte[] downloadFile(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();
        
        return s3Client.getObject(getObjectRequest).readAllBytes();
    }
    
    @Override
    public void deleteFile(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();
        
        s3Client.deleteObject(deleteObjectRequest);
    }
    
    @Override
    public String generateUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", 
            bucketName, region, key);
    }
}
