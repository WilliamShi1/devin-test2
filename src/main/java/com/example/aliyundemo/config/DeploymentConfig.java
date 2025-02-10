package com.example.aliyundemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "aliyun.deployment")
@Data
public class DeploymentConfig {
    private String ecsHost;
    private String sshUsername;
    private String sshPrivateKey;
    private String region;
}
