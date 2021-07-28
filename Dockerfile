FROM openjdk:8
VOLUME /tmp
EXPOSE 1238
ADD ./target/zuul-service-0.0.1-SNAPSHOT.jar zuul-service.jar
ENTRYPOINT ["java", "-jar", "/zuul-service.jar"]