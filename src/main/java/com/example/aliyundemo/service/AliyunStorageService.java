package com.example.aliyundemo.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.example.aliyundemo.model.FileMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.provider", havingValue = "aliyun", matchIfMissing = true)
public class AliyunStorageService implements StorageService {
    private final OSS ossClient;
    
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    
    @Override
    public FileMetadata uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String ossKey = UUID.randomUUID().toString() + "_" + fileName;
        
        PutObjectResult result = ossClient.putObject(bucketName, ossKey, file.getInputStream());
        
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
        OSSObject ossObject = ossClient.getObject(bucketName, ossKey);
        return ossObject.getObjectContent().readAllBytes();
    }
    
    @Override
    public void deleteFile(String ossKey) {
        ossClient.deleteObject(bucketName, ossKey);
    }
    
    @Override
    public String generateUrl(String ossKey) {
        return String.format("https://%s.%s/%s", bucketName, 
            ossClient.getBucketLocation(bucketName), ossKey);
    }
}
