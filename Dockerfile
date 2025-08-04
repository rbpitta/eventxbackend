# Stage 1: Build the Spring Boot application
# Usamos uma imagem Maven com JDK 15
FROM maven:3.8.7-jdk-15 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o Maven pom.xml primeiro para baixar as dependências
# Isso aproveita o cache de camadas do Docker. Se pom.xml não mudar,
# as dependências não serão baixadas novamente em builds subsequentes.
COPY pom.xml .

# Baixa as dependências (somente se pom.xml mudou)
RUN mvn dependency:go-offline

# Copia o restante do código-fonte da aplicação
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests

# Stage 2: Cria a imagem final leve para execução
# Usamos uma imagem OpenJDK 15 slim para ser menor e mais rápida
FROM openjdk:15-jdk-slim

# Expõe a porta padrão que sua aplicação Spring Boot escuta
EXPOSE 8080

# Copia o arquivo JAR gerado da etapa de build
# O padrão é que o JAR esteja em target/<nome-do-seu-jar>.jar
# Se o seu JAR tiver um nome muito específico, pode ser necessário ajustar '*.jar'
COPY --from=build /app/target/*.jar app.jar

# Executa a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]