# Build stage
FROM gradle:8.4.0-jdk21 as build

WORKDIR /app

# Copiar apenas os arquivos necessários para o build
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

# Construir o aplicativo
RUN gradle clean build --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copiar o JAR construído
COPY --from=build /app/build/libs/*.jar app.jar

# Expor a porta que o aplicativo Spring Boot usa
EXPOSE 8080

# Comando para executar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
