# syntax=docker/dockerfile:1
# Use a specific version of the base image for reproducibility and stability
FROM eclipse-temurin:17-jdk-jammy
# Use a different base image for the build stage
FROM maven:3.9.2-eclipse-temurin-17
# Set the working directory
WORKDIR /moonlight
# Copy the project files
COPY . /moonlight
# Expose the port on which the Spring Boot application will run
EXPOSE 8081
# Specify the default command to run the application
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=mysql", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081'"]
# Build the application using Maven
RUN mvn install
# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/moonlight/target/bootcamp-java-23.jar"]
