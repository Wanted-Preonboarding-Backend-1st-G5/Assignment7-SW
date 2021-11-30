FROM gradle AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# jar 파일 생성
RUN gradle build --no-daemon --exclude-task test

FROM openjdk:11-jre-slim as builder
WORKDIR app
ARG JAR_FILE=/home/gradle/src/build/libs/*-SNAPSHOT.jar
# Container 내부에 생성된 jar 파일 복사
COPY --from=build ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Create the containerized application
FROM openjdk:11-jre-slim
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
COPY wait-for-it.sh /app/src/wait-for-it.sh

# 파일 실행을 위한 권한 변경
RUN ["chmod", "+x", "/app/src/wait-for-it.sh"]
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]