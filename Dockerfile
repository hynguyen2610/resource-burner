FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Copy Gradle Wrapper files
COPY gradlew /app/
COPY gradle /app/gradle

# Copy project files
COPY build.gradle.kts settings.gradle.kts /app/
COPY src /app/src

# Ensure the Gradle Wrapper script is executable
RUN chmod +x gradlew

# Build the project using the Gradle Wrapper
RUN ./gradlew build

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8500

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
