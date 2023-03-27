FROM registry.access.redhat.com/ubi9/openjdk-17:1.14-2 as builder

COPY --chown=default . .

RUN ./gradlew copyJarToServerJar && ls build/libs

FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:1.14-2

COPY --from=builder --chown=default /home/default/build/libs/server.jar .

EXPOSE 8080

CMD java -jar server.jar
