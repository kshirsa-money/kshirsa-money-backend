# Use the official OpenJDK base image for Java 21
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar to the container

COPY /build/libs /app/build
COPY /newrelic/newrelic.jar /app/newrelic/newrelic.jar
COPY /newrelic/newrelic.yml /app/newrelic/newrelic.yml

# Expose the port that your Spring Boot application will run on
EXPOSE 8500

# Specify the command to run on container startup
CMD ["java", "-javaagent:/app/newrelic/newrelic.jar","-jar", "/app/build/Kshirsa-0.1.1-alpha.jar"]