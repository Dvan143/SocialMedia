FROM openjdk
WORKDIR /app
COPY target/SocialMedia-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080