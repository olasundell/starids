FROM openjdk:8u121
VOLUME /tmp
ADD build/libs/hazelcast-client.jar app.jar
RUN sh -c 'touch /app.jar'
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Dspring.profiles.active=docker -Dserver.port=$SERVER_PORT"
EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
