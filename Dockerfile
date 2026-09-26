FROM mcr.microsoft.com/playwright/java:v1.58.0-noble

WORKDIR /app

COPY target/ScheduleBotHTK-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]