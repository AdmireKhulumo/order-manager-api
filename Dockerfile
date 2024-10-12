# Use a base image with JDK to build the JAR file
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
RUN ls -a

# Use a minimal base image to run the JAR
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar order_manager.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "order_manager.jar"]
