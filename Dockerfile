FROM hirokimatsumoto/alpine-openjdk-11

COPY tokens/ /tokens/
RUN ls -la /tokens/*

ENV JDBC_DATABASE_URL ${JDBC_DATABASE_URL}
ENV JDBC_DATABASE_USERNAME ${JDBC_DATABASE_USERNAME}
ENV JDBC_DATABASE_PASSWORD ${JDBC_DATABASE_PASSWORD}

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]
