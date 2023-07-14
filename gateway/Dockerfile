FROM openjdk:11
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./gateway-1.0-SNAPSHOT.jar ./gateway.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar"]
CMD ["gateway.jar"]