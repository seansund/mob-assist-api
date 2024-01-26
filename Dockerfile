FROM registry.access.redhat.com/ubi9/openjdk-17:1.17-1.1705573248 as builder

COPY --chown=default . .

RUN ./gradlew copyJarToServerJar && ls build/libs

FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:1.17-1.1705573249

COPY --from=builder --chown=default /home/default/build/libs/server.jar .

EXPOSE 8080

CMD java -jar server.jar
