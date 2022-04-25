FROM openjdk:11-jdk

ARG JAV_FILE=./build/libs/*.jar

COPY ${JAV_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar","--spring.config.location=file:/src/main/resoucres/application.yml"]