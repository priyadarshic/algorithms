# Stage 1: Build the application using Maven
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project and skip tests for faster iteration
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/GraphsTrees-1.0-SNAPSHOT.jar app.jar

# Run the project's main class: HashMapDemo
ENTRYPOINT ["java", "-cp", "app.jar", "com.practice.datastructures.HashMapDemo"]
