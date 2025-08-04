# Stage 1: Build the Spring Boot application
FROM openjdk:15-jdk-slim AS build

# Define o diretório de trabalho antes de tudo para consistência
WORKDIR /app

# Instalar Maven
# Definimos as variáveis ARG e ENV aqui. ENV define variáveis de ambiente persistentes.
ARG MAVEN_VERSION=3.9.6
# Usando a URL de arquivo para maior estabilidade
ARG BASE_URL=https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries
ENV MAVEN_HOME /usr/local/maven
ENV PATH $MAVEN_HOME/bin:$PATH

# Instala ferramentas necessárias (wget, ca-certificates) e baixa/extrai o Maven
RUN apt-get update && \
    apt-get install -y --no-install-recommends wget ca-certificates && \
    wget -q ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz -O /tmp/apache-maven.tar.gz && \
    # CORREÇÃO AQUI: "/tmp/apache-maven.tar.gz" em vez de "/tmp/apache/maven.tar.gz"
    tar -xzf /tmp/apache-maven.tar.gz -C /usr/local && \
    mv /usr/local/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    rm /tmp/apache-maven.tar.gz && \
    apt-get purge -y --auto-remove wget ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Agora que o Maven está instalado e no PATH, podemos usar 'mvn'

# Copia o Maven pom.xml primeiro para baixar as dependências
COPY pom.xml .

# Baixa as dependências (somente se pom.xml mudou)
RUN mvn dependency:go-offline

# Copia o restante do código-fonte da aplicação
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests

# Stage 2: Cria a imagem final leve para execução
FROM openjdk:15-jdk-slim

# Expõe a porta padrão que sua aplicação Spring Boot escuta
EXPOSE 8080

# Copia o arquivo JAR gerado da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Executa a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]