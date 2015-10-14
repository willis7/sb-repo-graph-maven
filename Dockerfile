#   Usage:
#   # Build the Docker image
#   docker build -t maven-graph .

FROM java:8
VOLUME /tmp
ADD build/libs/sb-repo-graph-maven-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]