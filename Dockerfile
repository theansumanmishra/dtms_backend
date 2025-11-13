# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# leverage dependency layer caching
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar (first jar in target/)
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} app.jar

# Tune JVM for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
# Render injects PORT; your app already has server.port=${PORT:8080}
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
