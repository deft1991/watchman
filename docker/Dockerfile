FROM gradle:jdk21 as builder

WORKDIR /app

COPY . /app

RUN gradle build

FROM openjdk:22-slim

COPY --from=builder /app/watchman-service/build/libs/[^_]*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=dev,local","-jar","/app/app.jar"]
