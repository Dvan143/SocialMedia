## Hi there, my name is Dmitriy
### I am Java Spring Boot developer
I made this project for practice in many technologies: web development on Spring Framework, using PostgreSQL, using RabbitMq and other technologies.

Skills: Java / Spring Framework / RabbitMq / PostgreSQL / JS / HTML / CSS / Docker

### How to set up the project
Step 1: Create a .env file in the project root

Step 2.1: Make sure that you use application dev profile in application.yml or proceed to step 2.2

Step 2.2: Create and fill .env it in using the example.env file as a example

Step 2.3: If you want EmailNotification service working you must clone https://github.com/Dvan143/EmailVerifier repository


### How to start in docker:
Step 1: Create SocialMedia and EmailVerifier as conatiners

Step 2: Pull any SQL database and RabbitMq from DockerHub

Step 3: Set up the .env file

Step 4: Start all docker conatiners(compose.yml included)

Step 5: You are awesome! 

#### In code:
Package project to war file.

Create Dockerfile for this project. For example:
```Dockerfile
FROM tomcat:11.0.8-jdk21-temurin-noble
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/SocialMedia-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/socialmedia.war
COPY DefaultPage.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
```
Create Docker image from the Dockerfile
```
docker build -t socialmedia .
```
After setting the .env file, you can start the Docker containers using Docker Compose.
```
docker compose --env-file .env up
```
### You can test the project at site https://vishnivetskiyprojects.ru/socialmedia
#### or visit my web site with many projects https://vishnivetskiyprojects.ru

### Licenses

This project uses the Docker image boky/postfix from Docker Hub.
© [bokysan](https://github.com/bokysan), licensed under the MIT License.
