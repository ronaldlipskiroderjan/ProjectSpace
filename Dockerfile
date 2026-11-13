FROM maven:3.9-eclipse-temurin-21-alpine AS maven-base

ARG LOCAL_UID=1000
ARG LOCAL_GID=1000

RUN addgroup -g "${LOCAL_GID}" cucumber \
    && adduser -D -u "${LOCAL_UID}" -G cucumber cucumber \
    && mkdir -p /workspace /home/cucumber/.m2 \
    && chown -R cucumber:cucumber /workspace /home/cucumber

WORKDIR /workspace

ENV HOME=/home/cucumber \
    MAVEN_CONFIG=/home/cucumber/.m2

COPY --chown=cucumber:cucumber pom.xml ./

USER cucumber

RUN mvn -B -ntp dependency:go-offline

COPY --chown=cucumber:cucumber src src

FROM maven-base AS test

CMD ["mvn", "-B", "-ntp", "-Dtest=CucumberTest", "test"]

FROM maven-base AS build

RUN mvn -B -ntp -Dmaven.test.skip=true package

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app
COPY --from=build --chown=spring:spring /workspace/target/*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
