FROM openjdk:17-oracle

WORKDIR app

ARG JAV_FILE=./build/libs/*.jar

COPY ${JAV_FILE} ./

EXPOSE 8080

ENTRYPOINT ["java","-jar","./auth-0.0.1-SNAPSHOT.jar"]