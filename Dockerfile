FROM eclipse-temurin:21-jdk
WORKDIR /app
LABEL maintainer = "javaguides.net"
ADD target/demo-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]