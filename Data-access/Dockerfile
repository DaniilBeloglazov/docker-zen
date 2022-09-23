FROM openjdk:18
WORKDIR /data-access
ADD ./DAO-0.0.1-SNAPSHOT.jar data-access-server.jar
ENTRYPOINT ["java", "-jar", "data-access-server.jar"]