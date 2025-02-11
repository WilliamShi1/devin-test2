package com.example.aliyundemo.service;

import com.example.aliyundemo.model.FileMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.ResponseInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AwsS3ServiceTest {
    @Mock
    private S3Client s3Client;
    
    private AwsS3Service awsS3Service;
    
    @BeforeEach
    void setUp() {
        awsS3Service = new AwsS3Service(s3Client);
    }
    
    @Test
    void uploadFile_ShouldUploadToS3() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.txt", "text/plain", "test content".getBytes()
        );
        
        FileMetadata result = awsS3Service.uploadFile(file);
        
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertNotNull(result.getOssKey());
        assertEquals(file.getContentType(), result.getContentType());
        assertEquals(file.getSize(), result.getFileSize());
    }
    
    @Test
    void downloadFile_ShouldDownloadFromS3() throws Exception {
        String ossKey = "test-key";
        byte[] testContent = "test content".getBytes();
        
        ResponseInputStream<GetObjectResponse> response = mock(ResponseInputStream.class);
        when(response.readAllBytes()).thenReturn(testContent);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(response);
        
        byte[] result = awsS3Service.downloadFile(ossKey);
        
        verify(s3Client).getObject(any(GetObjectRequest.class));
        assertArrayEquals(testContent, result);
    }
    
    @Test
    void deleteFile_ShouldDeleteFromS3() {
        String ossKey = "test-key";
        
        awsS3Service.deleteFile(ossKey);
        
        verify(s3Client).deleteObject(any(DeleteObjectRequest.class));
    }
    
    @Test
    void generateUrl_ShouldGenerateValidUrl() {
        String ossKey = "test-key";
        String expectedUrl = "https://test-bucket.s3.amazonaws.com/test-key";
        
        when(s3Client.utilities().getUrl(any(GetUrlRequest.class)))
            .thenReturn(new java.net.URL(expectedUrl));
        
        String result = awsS3Service.generateUrl(ossKey);
        
        assertEquals(expectedUrl, result);
    }
}
