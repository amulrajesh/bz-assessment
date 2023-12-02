FROM openjdk:17-jdk-slim

# Refer to Maven build -> finalName
ARG JAR_FILE=target/bz-assessment-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} bz-assessment.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "bz-assessment.jar"]