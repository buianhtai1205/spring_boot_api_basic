FROM openjdk:17

WORKDIR /app
COPY target/study-spring-boot-0.0.1-SNAPSHOT.jar study-spring-boot-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java", "-jar" , "study-spring-boot-0.0.1-SNAPSHOT.jar" ]