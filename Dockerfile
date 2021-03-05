FROM adoptopenjdk:11-jre-hotspot
RUN apt-get update && apt-get install -y gettext-base
COPY target/consent-manager-back-uberjar-h2-*-runner.jar /consent-manager-back-h2.jar
COPY fs /
VOLUME /data
EXPOSE 8087
ENTRYPOINT ["/scripts/docker-entrypoint.sh"]
