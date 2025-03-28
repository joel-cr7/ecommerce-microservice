FROM maven:3-eclipse-temurin-23-alpine

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY /src ./src

ENTRYPOINT ["mvn", "spring-boot:run"]