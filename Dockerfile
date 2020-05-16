#Tell Docker to use a given image, tagged with version
FROM openjdk:11

#Arguements
ARG JAR_FILE=target/stockproject-0.0.1-SNAPSHOT.jar

#Copy the argument jar file into the image as app.jar
COPY ${JAR_FILE} app.jar

#Telling Docker which port our application is using. Port will be published to host
EXPOSE 8080

#Defining environment variables 
ENV FINNHUB_API_KEY=$FINNHUB_API_KEY

#Specifies the executable to start when the container is booting
ENTRYPOINT ["java","-jar","/app.jar"]