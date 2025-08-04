# Stage 1: Build the Spring Boot application
# Usamos a imagem oficial do Maven com Eclipse Temurin (OpenJDK) 17.
# '3.9.11-eclipse-temurin-17' é uma tag robusta para Maven 3.9.11 com JDK 17.
FROM maven:3.9.11-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do contêiner para o projeto
WORKDIR /app

# Copia o arquivo pom.xml primeiro para que o Docker possa cachear as dependências.
# Se o pom.xml não mudar, esta camada não será reconstruída em builds futuros.
COPY pom.xml .

# Baixa as dependências do Maven.
# 'dependency:go-offline' garante que todas as dependências necessárias sejam baixadas.
RUN mvn dependency:go-offline

# Copia o restante do código-fonte da aplicação.
# Isso invalidará a camada de build apenas se os arquivos-fonte mudarem.
COPY src ./src

# Compila a aplicação e empacota-a em um JAR executável.
# '-DskipTests' é usado para pular a execução de testes durante o build do Docker,
# o que acelera o processo. Testes devem ser executados no CI/CD antes do deploy.
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image for execution
# Usamos uma imagem JRE (Java Runtime Environment) leve baseada em OpenJDK 17.
# 'eclipse-temurin:17-jre-alpine' é uma excelente escolha por ser muito pequena,
# ideal para a imagem final de produção, contendo apenas o essencial para rodar o JAR.
# Alternativa leve (um pouco maior, baseada em Debian): openjdk:17-jre-slim-bullseye
FROM eclipse-temurin:17-jre-alpine

# Expõe a porta padrão que sua aplicação Spring Boot escuta (geralmente 8080).
EXPOSE 8080

# Copia o arquivo JAR gerado da etapa de build (primeiro estágio) para esta etapa final.
# O nome do JAR geralmente segue o artifactId e version do seu pom.xml.
# Assumindo que seu artifactId é 'demo' e version '0.0.1-SNAPSHOT', o nome será 'demo-0.0.1-SNAPSHOT.jar'.
# '*' garante que pegue qualquer nome de JAR.
COPY --from=build /app/target/*.jar app.jar

# Define o comando de entrada para executar a aplicação Spring Boot.
ENTRYPOINT ["java", "-jar", "app.jar"]