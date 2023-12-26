# Build the base app
FROM gradle:8-jdk21 as build
WORKDIR /app

COPY watchman-service/src ./src
COPY watchman-service/build.gradle ./build.gradle
COPY /settings.gradle ./settings.gradle

RUN gradle build && \
    rm -f build/libs/*-plain.jar

# Create a JRE runtime
FROM openjdk:21-jdk as runtime
USER root
RUN ${JAVA_HOME}/bin/jlink \
        --add-modules java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.unsupported \
        --verbose \
        --strip-debug \
        --no-man-pages \
        --no-header-files \
        --compress=zip-6 \
        --output /jre
USER app

# Final stage
FROM debian:stable-slim as final
WORKDIR /app

COPY --from=runtime /jre /jre
COPY --from=build /app/starter/build/libs/*.jar app.jar
# Setup path to JRE
ENV PATH="/jre/bin:${PATH}"

EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar "$@"
