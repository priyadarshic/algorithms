# Chapter: Introduction to Docker

Docker is a platform for developing, shipping, and running applications using **containers**. Containers are lightweight, standalone, and executable packages that include everything needed to run an application: code, runtime, system tools, system libraries, and settings.

## Why use Docker for GraphsTrees?

- **Consistency**: The `GraphsTrees` project depends on Java 17 and Maven. Docker ensures it runs the same way on your machine, a colleague's machine, or a server.
- **Isolation**: You don't need to install Java or Maven on your host machine to run the project.
- **Portability**: Once containerized, the project can be deployed easily to any Docker-capable environment (like Kubernetes).

## Containerizing the Project

To containerize this project, we use a `Dockerfile`. Here is a real-world example tailored for `GraphsTrees`.

### The Dockerfile

```dockerfile
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
```

## Practical Examples

### 1. Building the Docker Image
Navigate to the project root and run:
```bash
docker build -t graphstrees:v1 .
```

### 2. Running the Container
After building, run the `HashMapDemo` inside the container:
```bash
docker run --name graph-demo graphstrees:v1
```
*Expected Output*: You will see the output of `HashMapDemo.java` (internal structure of HashMap) printed to your terminal.

## Essential Docker Commands

| Command | Description |
| :--- | :--- |
| `docker build` | Build an image from a Dockerfile |
| `docker run` | Run a command in a new container |
| `docker images` | List available images |
| `docker ps` | List running containers |
| `docker stop` | Stop a running container |
| `docker rm` | Remove a container |
