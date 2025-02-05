
FROM openjdk:23-jdk-oracle AS builder

WORKDIR /compileddir

COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

ENV PORT=8080
EXPOSE ${PORT}

FROM openjdk:23-jdk-oracle

ARG WORK_DIR=/app
WORKDIR ${WORK_DIR}


COPY --from=builder /compileddir/target/day27-0.0.1-SNAPSHOT.jar ecommerce.jar


ENV PORT=8080
EXPOSE ${PORT}

ENV SPRING_DATA_REDIS_HOST=
ENV SPRING_DATA_REDIS_PORT=
ENV SPRING_DATA_REDIS_USERNAME=
ENV SPRING_DATA_REDIS_PASSWORD=

ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=



ENTRYPOINT SERVER_PORT=${PORT} java -jar ecommerce.jar

