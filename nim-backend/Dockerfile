FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Scene1: already build
COPY target ./target
# Scene2: need to build a release artifact:
# RUN mvn package -DskipTests

# Expose port for web service.
EXPOSE 8472

# Run the web service on container startup.
CMD ["java","-jar","/app/target/nim-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
