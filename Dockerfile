FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY target/spring_bootstrap-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "spring_bootstrap-0.0.1-SNAPSHOT.jar"]