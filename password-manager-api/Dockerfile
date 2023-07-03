FROM openjdk:20
EXPOSE 8080
RUN mkdir /src
RUN mkdir /src/main
RUN mkdir /app
COPY target/password-manager-api-*.jar /app/password-manager-api.jar
ENTRYPOINT ["java","-jar","app/password-manager-api.jar"]
