# Spring MVC Aliyun Demo

This is a Spring MVC application that integrates with Aliyun OSS and RDS, featuring a simple web interface for product management and file uploads.

## Configuration

The application uses environment variables for configuration. You need to set the following environment variables:

### Aliyun OSS Configuration
- `ALIYUN_OSS_ENDPOINT`: Aliyun OSS endpoint URL
- `ALIYUN_ACCESS_KEY_ID`: Your Aliyun Access Key ID
- `ALIYUN_ACCESS_KEY_SECRET`: Your Aliyun Access Key Secret
- `ALIYUN_OSS_BUCKET_NAME`: Your OSS bucket name

### Aliyun RDS Configuration
- `ALIYUN_RDS_URL`: Complete JDBC URL for your RDS instance
- `ALIYUN_RDS_USERNAME`: RDS database username
- `ALIYUN_RDS_PASSWORD`: RDS database password

### Deployment Configuration
- `ALIYUN_ECS_HOST`: Your ECS instance IP or hostname
- `ALIYUN_SSH_USERNAME`: SSH username for ECS instance
- `ALIYUN_SSH_PRIVATE_KEY`: SSH private key for ECS instance
- `ALIYUN_REGION`: Aliyun region (e.g., cn-hangzhou)

## Deployment

The application is automatically deployed to Aliyun ECS using GitHub Actions when changes are pushed to the main branch. The deployment workflow:

1. Builds the application
2. Runs tests
3. Packages the application
4. Deploys to the specified ECS instance

### GitHub Secrets

The following secrets need to be configured in your GitHub repository:

- `ALIYUN_ACCESS_KEY_ID`
- `ALIYUN_ACCESS_KEY_SECRET`
- `ALIYUN_REGION`
- `ALIYUN_ECS_HOST`
- `ALIYUN_ECS_USERNAME`
- `ALIYUN_ECS_SSH_KEY`

## Local Development

To run the application locally:

```bash
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080
# devintest
