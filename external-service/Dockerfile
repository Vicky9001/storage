FROM openjdk:11
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./external-service-1.0-SNAPSHOT.jar ./external.jar
EXPOSE 8101
ENTRYPOINT ["java","-jar"]
CMD ["external.jar"]