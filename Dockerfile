FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/module-02-final-project-1.0.jar mod2-final.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mod2-final.jar"]
