FROM quay.io/quarkus/centos-quarkus-maven:20.1.0-java11 AS build
COPY pom.xml /project
RUN mvn -f /project/pom.xml -B de.qaware.maven:go-offline-maven-plugin:1.2.5:resolve-dependencies
COPY src /project/src
RUN mvn -f /project/pom.xml clean package -DskipTests -Dquarkus.package.uber-jar=true

FROM adoptopenjdk:11-jre-openj9
COPY --from=build /project/target/*-runner.jar /consent-manager-back.jar
COPY fs /
VOLUME /data
EXPOSE 8087
ENTRYPOINT ["/scripts/docker-entrypoint.sh"]