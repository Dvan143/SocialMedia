FROM tomcat:11.0.8-jdk21-temurin-noble
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/SocialMedia-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/socialmedia.war
COPY DefaultPage.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080