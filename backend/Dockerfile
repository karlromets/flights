# Build stage: Compile the Java application
FROM maven:3.9.9-amazoncorretto-23 AS build
WORKDIR /app


COPY pom.xml .

# Download dependencies in a separate layer for better caching
RUN mvn dependency:go-offline

# Copy source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Create a smaller image for running the application
FROM amazoncorretto:23-alpine3.21-jdk AS runner
WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

# Run the Java application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
