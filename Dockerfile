# Stage 1: Build
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first to cache dependency downloads
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Download dependencies (cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -q

# Copy source code and build the application
COPY src src
RUN ./mvnw clean package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:25-jre

WORKDIR /app

# Create a non-root user for running the application
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

COPY --from=build /app/target/router-0.0.1-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
