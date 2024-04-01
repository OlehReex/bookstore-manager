## Stage 1: Build
#FROM gradle:8.6.0-jdk17-alpine AS builder
#WORKDIR /app
#COPY . .
#RUN gradle build --no-daemon
#
## Stage 2: Run
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY --from=builder /app/build/libs/*.jar application.jar
##COPY --from=builder /home/gradle/.gradle/caches/modules-2/files-2.1/io.grpc/protoc-gen-grpc-java/1.62.2/b1c262cdfd87bb557ef4a674815920df03751b6e/protoc-gen-grpc-java-1.62.2-windows-x86_64/*.jar application.jar
#ENTRYPOINT ["java", "-jar", "application.jar"]
#EXPOSE 8080
