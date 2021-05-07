FROM maven:3.6.3-jdk-8 AS MAVEN_BUILD

COPY . /build
WORKDIR /build

RUN mvn clean package

FROM openjdk:8-jdk-alpine

WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/emailAddressGenerator.jar /app/api-service.jar

EXPOSE 5000
ENTRYPOINT java -jar api-service.jar
