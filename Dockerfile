# Use uma imagem base que já contenha o JDK e o Maven
FROM maven:3.8.4-openjdk-17 AS build

# Crie um diretório de trabalho e copie o código-fonte para ele
WORKDIR /app
COPY . .

# Execute o comando Maven para construir o projeto
RUN mvn clean install

# Altere a imagem base para uma imagem menor que contenha apenas o JRE
FROM openjdk:17-jdk-slim

# Copie o artefato JAR gerado no estágio anterior
COPY --from=build /app/target/todolist-0.0.1.jar app.jar

# Exponha a porta em que o aplicativo será executado (se necessário)
EXPOSE 8080

# Comando de entrada para iniciar o aplicativo
CMD ["java", "-jar", "app.jar"]
