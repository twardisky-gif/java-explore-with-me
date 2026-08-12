FROM eclipse-temurin:21-jre

WORKDIR /app
COPY stats-service/stats-server/target/stats-server-*.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
