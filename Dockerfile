# Etapa de Build (compila o projeto)
FROM maven:3.8.7-jdk-17 AS build
# Define o diretório de trabalho dentro do contêiner
WORKDIR /app
# Copia todo o conteúdo do seu projeto para o diretório de trabalho
COPY . /app
# Compila o projeto Spring Boot, pulando os testes
RUN mvn clean package -DskipTests

# Etapa de Execução (roda a aplicação)
FROM maven:3.8.7-jdk-17 AS build
# Copia o arquivo JAR gerado da etapa de build para a etapa de execução
# O padrão é que o JAR esteja em target/<nome-do-seu-jar>.jar
# Verifique o nome exato do seu JAR após rodar mvn clean package localmente
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta padrão do Spring Boot
EXPOSE 8080
# Define o comando que será executado quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]