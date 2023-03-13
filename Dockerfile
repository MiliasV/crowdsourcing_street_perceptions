FROM maven:3.6-jdk-11 AS build

COPY . /usr/src/app

RUN mvn -f /usr/src/app/pom.xml -P docker clean package -Dmaven.test.skip=true

FROM openjdk:11

COPY --from=build /usr/src/app/annotation-server/target/subjectivity.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]