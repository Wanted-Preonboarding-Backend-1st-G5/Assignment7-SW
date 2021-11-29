FROM openjdk:11-jre-slim as builder
WORKDIR app
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11-jre-slim
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
COPY wait-for-it.sh /app/src/wait-for-it.sh
RUN ["chmod", "+x", "/app/src/wait-for-it.sh"]
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]