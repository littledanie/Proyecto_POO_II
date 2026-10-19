# -------------------------------------------------------
# Dockerfile — Gestiona Commerce (Tomcat 10.1 + JDK 17)
# Compilación multi-stage: Maven compila → Tomcat ejecuta
# -------------------------------------------------------

# === ETAPA 1: compilación ===
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar solo el pom primero para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src/ src/
RUN mvn clean package -DskipTests -B

# === ETAPA 2: imagen de ejecución ===
FROM tomcat:10.1-jdk17

# Eliminar la aplicación ROOT por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copiar el WAR generado como ROOT.war (se sirve en /)
COPY --from=build /app/target/gestiona-commerce.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
