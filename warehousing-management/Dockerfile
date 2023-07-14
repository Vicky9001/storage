FROM openjdk:11
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY ./warehousing-management-1.0-SNAPSHOT.jar ./warehousing.jar
EXPOSE 8102
ENTRYPOINT ["java","-jar"]
CMD ["warehousing.jar"]