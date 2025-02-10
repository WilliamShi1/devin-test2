package com.example.aliyundemo.controller;

import com.example.aliyundemo.model.FileMetadata;
import com.example.aliyundemo.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class OssController {
    private final StorageService storageService;
    
    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileMetadata metadata = storageService.uploadFile(file);
        return ResponseEntity.ok(metadata);
    }
    
    @GetMapping("/download/{ossKey}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String ossKey) throws IOException {
        byte[] data = storageService.downloadFile(ossKey);
        ByteArrayResource resource = new ByteArrayResource(data);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ossKey);
        
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(data.length)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }
    
    @DeleteMapping("/{ossKey}")
    public ResponseEntity<Void> deleteFile(@PathVariable String ossKey) {
        storageService.deleteFile(ossKey);
        return ResponseEntity.ok().build();
    }
}
