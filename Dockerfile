FROM eclipse-temurin:21-jdk
WORKDIR /app
LABEL maintainer = "javaguides.net"
ADD target/calculator-0.0.1-SNAPSHOT.jar calculator.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "calculator.jar"]