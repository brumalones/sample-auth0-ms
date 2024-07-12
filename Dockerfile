# Etapa 1: Construção do JAR
FROM maven:3.9.8-sapmachine-17 AS builder

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo pom.xml e o diretório src para dentro do contêiner
COPY . .

# Execute o Maven para construir o JAR
RUN mvn clean package -q -e -DskipTests

# Etapa 2: Criação da imagem final com o JAR
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho
WORKDIR /app

# Copie o JAR gerado da etapa de construção
COPY --from=builder /app/target/*.jar auth0-0.0.1-SNAPSHOT.jar

# Exponha a porta do aplicativo
EXPOSE 8761

# Comando para executar o JAR
ENTRYPOINT ["java", "-jar", "/app/auth0-0.0.1-SNAPSHOT.jar"]
