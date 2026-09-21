FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/service-foo-0.0.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]