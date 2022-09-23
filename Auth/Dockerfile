FROM openjdk:18
WORKDIR /auth
ADD ./Auth-0.0.1-SNAPSHOT.jar auth-server.jar
ENTRYPOINT ["java", "-jar", "auth-server.jar"]