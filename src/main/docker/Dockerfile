FROM adoptopenjdk:11-jre-openj9
COPY --from=build /project/target/*-runner.jar /consent-manager-back.jar
COPY fs /
VOLUME /data
EXPOSE 8087
ENTRYPOINT ["/scripts/docker-entrypoint.sh"]
