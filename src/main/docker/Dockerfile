#   Usage:
#   # Build the Docker image
#   docker build -t maven-graph .

FROM java:8
VOLUME /tmp
ADD *.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]