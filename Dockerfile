FROM eclipse-temurin:21-jdk-jammy
COPY . .
RUN ./mvnw clean install -DskipTests
ENTRYPOINT [ "java" , "-jar", "target/OPDEE-0.0.1-SNAPSHOT.jar"]