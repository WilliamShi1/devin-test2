package com.example.aliyundemo.service;

import com.example.aliyundemo.model.FileMetadata;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Common interface for cloud storage operations.
 * Implementations will be provided for both Aliyun OSS and AWS S3.
 */
public interface StorageService {
    /**
     * Upload a file to cloud storage
     * @param file The file to upload
     * @return Metadata about the uploaded file
     * @throws IOException If there's an error during upload
     */
    FileMetadata uploadFile(MultipartFile file) throws IOException;

    /**
     * Download a file from cloud storage
     * @param key The unique identifier of the file
     * @return The file contents as a byte array
     * @throws IOException If there's an error during download
     */
    byte[] downloadFile(String key) throws IOException;

    /**
     * Delete a file from cloud storage
     * @param key The unique identifier of the file to delete
     */
    void deleteFile(String key);

    /**
     * Generate a URL for accessing the file
     * @param key The unique identifier of the file
     * @return A URL that can be used to access the file
     */
    String generateUrl(String key);
}
