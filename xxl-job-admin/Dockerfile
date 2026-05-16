FROM openjdk:11

EXPOSE 5000

ARG JAR_FILE=xxl-job-admin.jar

ADD target/$JAR_FILE /xxl-job-admin.jar

ENTRYPOINT ["java","-jar","/xxl-job-admin.jar"]