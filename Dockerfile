FROM --platform=linux/amd64 openjdk:22
LABEL maintainer="Janis"
EXPOSE 8080
ADD backend/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]