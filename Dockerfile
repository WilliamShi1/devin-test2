FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the JAR file
COPY target/*.jar app.jar

# Copy the configuration files
COPY src/main/resources/application.properties /app/config/
COPY src/main/resources/application-*.properties /app/config/
COPY src/main/resources/messages*.properties /app/config/

# Set environment variables
ENV SPRING_CONFIG_LOCATION=file:/app/config/

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
