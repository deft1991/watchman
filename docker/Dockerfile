# Build the base app
# Use a base image with Java and Gradle installed
FROM gradle:8-jdk21 AS builder

# Set working directory in the container
WORKDIR /app

COPY . /app

RUN gradle multi-starter-service:build -x test && \
    rm -f multi-starter-service/build/libs/*-plain.jar

# Use a lightweight base image with Java installed
FROM openjdk:21-jdk

# Set working directory in the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/multi-starter-service/build/libs/*.jar app.jar

# Setup path to JRE
ENV PATH="/jre/bin:${PATH}"

EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar "$@"

# Command to run the application
#CMD ["java", "-jar", "app.jar"]
