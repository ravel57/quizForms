FROM gradle:8.7.0-jdk17 AS gradle
WORKDIR /home/gradle/
RUN ["gradle", "war"]

FROM tomcat:10.1.23-jre21 AS tomcat9
COPY --from=gradle /home/gradle/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war
CMD chmod +x /usr/local/tomcat/bin/catalina.sh
CMD ["catalina.sh", "run"]