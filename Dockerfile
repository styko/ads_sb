FROM maven:3.6.3-openjdk-11-slim AS build-stage 
WORKDIR /app
COPY src ./src
COPY pom.xml . 
RUN mvn clean package 

FROM gcr.io/distroless/java:11 AS production-stage
WORKDIR /app
COPY --from=build-stage /app/target/*.jar .
COPY tokens/ /tokens/

ENV JDBC_DATABASE_URL ${JDBC_DATABASE_URL}
ENV JDBC_DATABASE_USERNAME ${JDBC_DATABASE_USERNAME}
ENV JDBC_DATABASE_PASSWORD ${JDBC_DATABASE_PASSWORD}

EXPOSE 8080
#ENTRYPOINT ["java","-jar","realestate_email_aggregator-1.0-SNAPSHOT.jar","--spring.profiles.active=prod"]
CMD ["realestate_email_aggregator-1.0-SNAPSHOT.jar --spring.profiles.active=prod"]
