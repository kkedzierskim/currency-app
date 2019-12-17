FROM openjdk:8
ADD target/currency-app-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar currency-app-0.0.1-SNAPSHOT.jar

