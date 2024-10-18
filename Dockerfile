#
# Build stage
#
# Use the official Maven/Java 17 image

#TYPE 1
FROM maven:3.8.3-openjdk-17 AS build
LABEL authors="Ace Nghiem"
COPY ./src /home/booking/src
COPY pom.xml /home/booking
RUN mvn -f /home/booking/pom.xml clean package
#Specify that expose server port
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/booking/target/booking.jar"]


#TYPE2
# Use the official Maven/Java 17 image
#FROM maven:3.8.1-openjdk-17-slim AS build

#LABEL authors="Ace Nghiem"

# Set the working directory in the container
#WORKDIR /booking

# Copy the project files into the container
#COPY pom.xml .
#COPY ./src ./src

# Build the application
#RUN mvn package

# Create the final image
#FROM adoptopenjdk/openjdk17:alpine

# Set the working directory in the container
#WORKDIR /booking

# Copy the JAR file from the build stage to the final image
#COPY --from=build /booking/target/*.jar app.jar

# Expose the port
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/home/booking/target/booking.jar"]