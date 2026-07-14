# ============================================
# ETAPA 1: COMPILACIÓN
# ============================================
FROM maven:3.9.9-eclipse-temurin-21 AS build

ENV MAVEN_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m"

WORKDIR /app

# Copiar pom.xml desde la carpeta elegance/
COPY elegance/pom.xml .

# Copiar el resto del código fuente desde elegance/
COPY elegance/src ./src

# Descargar dependencias y compilar
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests -B

# ============================================
# ETAPA 2: EJECUCIÓN
# ============================================
FROM eclipse-temurin:21-jre-alpine

# Crear usuario no-root
RUN addgroup -S -g 1000 app && \
    adduser -S -u 1000 -G app app

# Instalar dependencias
RUN apk update && \
    apk upgrade --no-cache && \
    apk add --no-cache ca-certificates tzdata && \
    rm -rf /var/cache/apk/*

WORKDIR /app

# Copiar el JAR desde la etapa de compilación
COPY --from=build --chown=app:app /app/target/*.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Duser.timezone=America/Lima", \
    "-jar", "app.jar"]