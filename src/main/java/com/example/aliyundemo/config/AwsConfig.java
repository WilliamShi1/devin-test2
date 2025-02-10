package com.example.aliyundemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
@ConditionalOnProperty(name = "storage.provider", havingValue = "aws")
public class AwsConfig {
    
    @Value("${aws.region}")
    private String region;
    
    @Value("${aws.access-key-id:#{null}}")
    private String accessKeyId;
    
    @Value("${aws.secret-key:#{null}}")
    private String secretKey;
    
    @Bean
    public S3Client s3Client() {
        S3ClientBuilder builder = S3Client.builder()
            .region(Region.of(region));
            
        // If credentials are provided via properties, use them
        // Otherwise, fall back to default credential provider chain (IAM roles, etc.)
        if (accessKeyId != null && secretKey != null) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretKey)
            ));
        }
        
        return builder.build();
    }
}
