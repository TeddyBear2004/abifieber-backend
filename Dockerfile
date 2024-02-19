FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/AbiballShop-1.0-SNAPSHOT.jar ./app.jar
COPY hibernate.cfg.xml.template ./hibernate.cfg.xml.template
EXPOSE 7000

# Install gettext for envsubst
RUN apt-get update && apt-get install -y gettext-base

CMD envsubst < hibernate.cfg.xml.template > hibernate.cfg.xml && java -jar app.jar
