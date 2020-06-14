FROM maven:3.6.3-openjdk-11-slim AS build-stage
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package

FROM hirokimatsumoto/alpine-openjdk-11 AS production-stage
WORKDIR /app
COPY --from=build-stage /app/target/*.jar .
RUN ls -la
COPY tokens/ /tokens/
RUN ls -la /tokens/*

ENV APP_NAME ${APP_NAME}
ENV CREDENTIALS ${CREDENTIALS}
ENV JDBC_DATABASE_URL ${JDBC_DATABASE_URL}
ENV JDBC_DATABASE_USERNAME ${JDBC_DATABASE_USERNAME}
ENV JDBC_DATABASE_PASSWORD ${JDBC_DATABASE_PASSWORD}

EXPOSE 8080
#CMD ["java -jar /app/realestate_email_aggregator-1.0-SNAPSHOT.jar"]
ENTRYPOINT ["java","-jar","realestate_email_aggregator-1.0-SNAPSHOT.jar","--spring.profiles.active=prod"]

