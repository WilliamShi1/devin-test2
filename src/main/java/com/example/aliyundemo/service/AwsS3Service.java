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
public class AwsS3Service implements StorageService {
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    
    @Override
    public FileMetadata uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String ossKey = UUID.randomUUID().toString() + "_" + fileName;
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(ossKey)
                .contentType(file.getContentType())
                .build();
                
        s3Client.putObject(putObjectRequest, 
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(fileName);
        metadata.setOssKey(ossKey);
        metadata.setContentType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setUploadTime(LocalDateTime.now());
        metadata.setUrl(generateUrl(ossKey));
        
        return metadata;
    }
    
    @Override
    public byte[] downloadFile(String ossKey) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(ossKey)
                .build();
                
        return s3Client.getObject(getObjectRequest).readAllBytes();
    }
    
    @Override
    public void deleteFile(String ossKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(ossKey)
                .build();
                
        s3Client.deleteObject(deleteObjectRequest);
    }
    
    @Override
    public String generateUrl(String ossKey) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(ossKey)
                .build();
        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }
}
