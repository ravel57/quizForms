FROM gradle:8.6.0-jdk17 AS gradle
EXPOSE 8082
COPY --chown=gradle:gradle . /home/gradle/
WORKDIR /home/gradle/
RUN gradle bootJar
CMD ["java", "-jar", "build/libs/quizForms-1.jar"]